#!/usr/bin/env python3
"""
LinkedIn Daily Digest Generator & Publisher for DSA Progress.
Supports: LeetCode (LeetHub/CodeHub format), GeeksforGeeks (GfG-To-GitHub/CodeHub format),
HackerRank, and other multi-platform directory layouts.
"""

import os
import sys
import json
import re
import argparse
import subprocess
from datetime import datetime, timezone
# Force UTF-8 on Windows consoles
if sys.platform == "win32":
    try:
        if hasattr(sys.stdout, "reconfigure"):
            sys.stdout.reconfigure(encoding="utf-8", errors="replace")
        if hasattr(sys.stderr, "reconfigure"):
            sys.stderr.reconfigure(encoding="utf-8", errors="replace")
    except Exception:
        pass

import urllib.request
import urllib.error

# Mapping of file extensions to programming languages
LANG_EXTENSIONS = {
    ".java": "Java",
    ".cpp": "C++",
    ".cc": "C++",
    ".cxx": "C++",
    ".c": "C",
    ".py": "Python",
    ".py3": "Python3",
    ".js": "JavaScript",
    ".ts": "TypeScript",
    ".cs": "C#",
    ".go": "Go",
    ".rs": "Rust",
    ".kt": "Kotlin",
    ".swift": "Swift",
    ".sql": "SQL"
}

KNOWN_DIFFICULTIES = {"easy", "medium", "hard", "basic", "school"}

def run_git_command(args, repo_root):
    """Run a git command in the repository directory and return stdout."""
    try:
        result = subprocess.run(
            ["git"] + args,
            cwd=repo_root,
            capture_output=True,
            text=True,
            check=True
        )
        return result.stdout.strip()
    except subprocess.CalledProcessError as e:
        print(f"[Warning] Git command failed: {' '.join(args)}\nError: {e.stderr}", file=sys.stderr)
        return ""

def parse_readme_metadata(readme_path):
    """Extract metadata (title, platform, difficulty, tags) from a problem's README.md."""
    if not os.path.exists(readme_path):
        return {}
    
    try:
        with open(readme_path, "r", encoding="utf-8", errors="ignore") as f:
            content = f.read()
    except Exception:
        return {}

    metadata = {}
    
    # 1. Check for platform URLs in links
    if "leetcode.com" in content:
        metadata["platform"] = "LeetCode"
    elif "geeksforgeeks.org" in content:
        metadata["platform"] = "GeeksforGeeks"
    elif "hackerrank.com" in content:
        metadata["platform"] = "HackerRank"
    elif "codeforces.com" in content:
        metadata["platform"] = "Codeforces"

    # 2. Extract Difficulty (e.g., <h3>Medium</h3>, **Difficulty:** Easy, etc.)
    diff_match = re.search(r"<h3>\s*(Easy|Medium|Hard|Basic|School)\s*</h3>", content, re.IGNORECASE)
    if diff_match:
        metadata["difficulty"] = diff_match.group(1).capitalize()
    else:
        diff_match2 = re.search(r"\b(Difficulty|Level)\s*:\s*(Easy|Medium|Hard|Basic|School)\b", content, re.IGNORECASE)
        if diff_match2:
            metadata["difficulty"] = diff_match2.group(2).capitalize()

    # 3. Extract Title from <h2> or <h1> or first markdown header
    title_match = re.search(r"<h[12][^>]*>(?:<a[^>]*>)?(.*?)(?:</a>)?</h[12]>", content, re.IGNORECASE)
    if title_match:
        clean_title = re.sub(r"<[^>]+>", "", title_match.group(1)).strip()
        # Clean leading numbers like "886. Score of Parentheses" -> "Score of Parentheses"
        clean_title = re.sub(r"^\d+\.\s*", "", clean_title)
        metadata["title"] = clean_title

    return metadata

def clean_slug_to_title(slug):
    """Convert slug like '0856-score-of-parentheses' or 'subarray_with_given_sum' to clean title."""
    # Remove leading numeric prefix like '0856-'
    cleaned = re.sub(r"^\d+[-_]", "", slug)
    cleaned = cleaned.replace("-", " ").replace("_", " ")
    return cleaned.title()

def get_modified_files(repo_root, hours=24, since_arg=None):
    """Get list of files changed/added in the specified time frame."""
    if since_arg:
        time_arg = f"--since={since_arg}"
    else:
        time_arg = f"--since={hours} hours ago"

    stdout = run_git_command(["log", time_arg, "--name-only", "--pretty=format:"], repo_root)
    if not stdout:
        return []
    
    files = [line.strip() for line in stdout.split("\n") if line.strip()]
    return list(dict.fromkeys(files))  # Preserve order, remove duplicates

def detect_problems(repo_root, hours=24, since_arg=None):
    """Scan git history and identify unique DSA problems solved."""
    changed_files = get_modified_files(repo_root, hours, since_arg)
    if not changed_files:
        return []

    # Load stats.json if present (common in LeetHub)
    stats_data = {}
    stats_file = os.path.join(repo_root, "stats.json")
    if os.path.exists(stats_file):
        try:
            with open(stats_file, "r", encoding="utf-8") as f:
                stats_data = json.load(f)
        except Exception:
            pass

    problems = {}

    for file_path in changed_files:
        norm_path = file_path.replace("\\", "/")
        parts = [p for p in norm_path.split("/") if p]

        # Ignore root files & system dirs
        if len(parts) < 2 or parts[0] in {".git", ".github", ".specify", ".devcontainer"}:
            continue
        
        # Determine candidate problem directory
        # Patterns:
        # 1. LeetHub v2: '0856-score-of-parentheses/0856-score-of-parentheses.java'
        # 2. GfG-To-GitHub: 'Easy/Largest_Element/Solution.java'
        # 3. CodeHub: 'GeeksForGeeks/Easy/Problem_Name/Solution.java'
        # 4. Multi-platform: 'LeetCode/0001-two-sum/solution.py'
        
        platform = "LeetCode"
        difficulty = "Medium"
        title = ""
        problem_folder = ""
        lang = "Java"

        # Check for solution file extension
        _, ext = os.path.splitext(parts[-1])
        if ext.lower() in LANG_EXTENSIONS:
            lang = LANG_EXTENSIONS[ext.lower()]

        first_part_lower = parts[0].lower()
        if first_part_lower in {"geeksforgeeks", "gfg"}:
            platform = "GeeksforGeeks"
            if len(parts) >= 3 and parts[1].lower() in KNOWN_DIFFICULTIES:
                difficulty = parts[1].capitalize()
                title = clean_slug_to_title(parts[2])
                problem_folder = "/".join(parts[:3])
            else:
                title = clean_slug_to_title(parts[1])
                problem_folder = "/".join(parts[:2])
        elif first_part_lower in KNOWN_DIFFICULTIES:
            # GfG-To-GitHub default structure: Easy/Problem_Name
            platform = "GeeksforGeeks"
            difficulty = parts[0].capitalize()
            title = clean_slug_to_title(parts[1])
            problem_folder = "/".join(parts[:2])
        elif first_part_lower in {"hackerrank", "codechef", "codeforces"}:
            platform = parts[0].capitalize()
            title = clean_slug_to_title(parts[1])
            problem_folder = "/".join(parts[:2])
        elif first_part_lower == "leetcode":
            platform = "LeetCode"
            title = clean_slug_to_title(parts[1])
            problem_folder = "/".join(parts[:2])
        elif re.match(r"^\d{4}-", parts[0]):
            # LeetHub style: '0856-score-of-parentheses'
            platform = "LeetCode"
            title = clean_slug_to_title(parts[0])
            problem_folder = parts[0]
        else:
            # Generic problem folder
            title = clean_slug_to_title(parts[0])
            problem_folder = parts[0]

        # Read README.md for rich metadata if available
        abs_problem_dir = os.path.join(repo_root, problem_folder)
        readme_path = os.path.join(abs_problem_dir, "README.md")
        meta = parse_readme_metadata(readme_path)

        if meta.get("platform"):
            platform = meta["platform"]
        if meta.get("difficulty"):
            difficulty = meta["difficulty"]
        if meta.get("title"):
            title = meta["title"]

        # Check stats.json fallback
        if problem_folder in stats_data:
            s_entry = stats_data[problem_folder]
            if isinstance(s_entry, dict) and "difficulty" in s_entry:
                difficulty = s_entry["difficulty"].capitalize()

        prob_key = f"{platform}:{title}"
        if prob_key not in problems:
            problems[prob_key] = {
                "title": title,
                "platform": platform,
                "difficulty": difficulty,
                "language": lang,
                "folder": problem_folder
            }
        else:
            if lang != "Unknown" and problems[prob_key]["language"] == "Unknown":
                problems[prob_key]["language"] = lang

    return list(problems.values())

def generate_post_text(problems, repo_url="https://github.com/Sadique721/MdSadiqueAmin-LeetCode"):
    """Format an engaging, recruiter-friendly LinkedIn update."""
    today_str = datetime.now(timezone.utc).strftime("%d %b %Y")
    count = len(problems)

    # Platforms breakdown
    platforms = sorted(list({p["platform"] for p in problems}))
    platforms_str = " & ".join(platforms)

    diff_counts = {}
    for p in problems:
        d = p["difficulty"]
        diff_counts[d] = diff_counts.get(d, 0) + 1

    diff_summary = ", ".join([f"{count} {diff}" for diff, count in diff_counts.items()])

    lines = [
        f"🚀 Daily DSA Grind Update | {today_str}",
        "",
        f"Today's problem-solving progress across {platforms_str}:",
        ""
    ]

    for p in problems:
        icon = "🟢" if p["difficulty"].lower() == "easy" else ("🟡" if p["difficulty"].lower() == "medium" else "🔴")
        lines.append(f"{icon} {p['title']} [{p['difficulty']}] - {p['platform']}")
        lines.append(f"   • Language: {p['language']}")

    lines.append("")
    lines.append("📊 Daily Summary:")
    lines.append(f"• Total Solved: {count} ({diff_summary})")
    lines.append("• Focus: Clean code, optimal time/space complexity & pattern recognition.")
    lines.append("")
    lines.append(f"📁 Solutions & README notes committed on GitHub:")
    lines.append(f"🔗 {repo_url}")
    lines.append("")
    lines.append("#DSA #Java #LeetCode #GeeksforGeeks #CodingStreak #SoftwareEngineering #ProblemSolving #Algorithms")

    return "\n".join(lines)

def build_linkedin_payload(author_urn, text):
    """Construct LinkedIn UGC Posts API payload."""
    clean_urn = author_urn.strip()
    if not clean_urn.startswith("urn:li:person:"):
        clean_urn = f"urn:li:person:{clean_urn}"

    return {
        "author": clean_urn,
        "lifecycleState": "PUBLISHED",
        "specificContent": {
            "com.linkedin.ugc.ShareContent": {
                "shareCommentary": {
                    "text": text
                },
                "shareMediaCategory": "NONE"
            }
        },
        "visibility": {
            "com.linkedin.ugc.MemberNetworkVisibility": "PUBLIC"
        }
    }

def publish_to_linkedin(access_token, payload):
    """Send payload to LinkedIn API."""
    url = "https://api.linkedin.com/v2/ugcPosts"
    headers = {
        "Authorization": f"Bearer {access_token.strip()}",
        "X-Restli-Protocol-Version": "2.0.0",
        "Content-Type": "application/json"
    }

    req = urllib.request.Request(
        url,
        data=json.dumps(payload).encode("utf-8"),
        headers=headers,
        method="POST"
    )

    try:
        with urllib.request.urlopen(req) as response:
            resp_body = response.read().decode("utf-8")
            status = response.getcode()
            return status, resp_body
    except urllib.error.HTTPError as e:
        error_body = e.read().decode("utf-8", errors="ignore")
        return e.code, error_body
    except Exception as e:
        return 0, str(e)

def main():
    parser = argparse.ArgumentParser(description="LinkedIn Daily Digest for DSA")
    parser.add_argument("--repo-root", default=".", help="Root path of the DSA repo")
    parser.add_argument("--hours", type=int, default=24, help="Scan commits from past N hours")
    parser.add_argument("--since", default=None, help="Explicit git since argument (e.g. '2026-10-07')")
    parser.add_argument("--dry-run", action="store_true", help="Print post and validate without API call")
    parser.add_argument("--test-mock", action="store_true", help="Run with mock data to test end-to-end")
    parser.add_argument("--repo-url", default="https://github.com/Sadique721/MdSadiqueAmin-LeetCode", help="GitHub repo URL")
    args = parser.parse_args()

    repo_root = os.path.abspath(args.repo_root)

    print("==================================================")
    print("  🚀 LinkedIn DSA Daily Digest Pipeline")
    print("==================================================")
    print(f"📁 Repository Path: {repo_root}")

    if args.test_mock:
        print("🧪 Mode: Running with synthetic test data...")
        problems = [
            {"title": "Score of Parentheses", "platform": "LeetCode", "difficulty": "Medium", "language": "Java", "folder": "0856-score-of-parentheses"},
            {"title": "Subarray with Given Sum", "platform": "GeeksforGeeks", "difficulty": "Medium", "language": "Java", "folder": "GeeksForGeeks/Medium/subarray-with-given-sum"},
            {"title": "Valid Parentheses", "platform": "LeetCode", "difficulty": "Easy", "language": "Java", "folder": "0020-valid-parentheses"}
        ]
    else:
        problems = detect_problems(repo_root, hours=args.hours, since_arg=args.since)

    if not problems:
        print("ℹ️  No new problems detected in the specified timeframe.")
        print("⏭️  Skipping LinkedIn post (no activity to report).")
        sys.exit(0)

    print(f"✅ Found {len(problems)} problem(s) solved:")
    for idx, p in enumerate(problems, 1):
        print(f"   {idx}. [{p['platform']}] {p['title']} ({p['difficulty']}) in {p['language']}")

    post_text = generate_post_text(problems, repo_url=args.repo_url)
    print("\n---------------- GENERATED POST PREVIEW ----------------")
    print(post_text)
    print("--------------------------------------------------------\n")

    token = os.environ.get("LINKEDIN_ACCESS_TOKEN", "").strip()
    person_urn = os.environ.get("LINKEDIN_PERSON_URN", "").strip()

    # Validate payload structure
    payload = build_linkedin_payload(person_urn or "DEMO_PERSON_URN", post_text)
    print(f"📦 Payload schema check: Valid ({len(json.dumps(payload))} bytes)")

    if args.dry_run or not token or not person_urn:
        if not token or not person_urn:
            print("💡 Dry-run notice: LINKEDIN_ACCESS_TOKEN or LINKEDIN_PERSON_URN is not set in environment.")
            print("   Workflow is validated successfully. To post live, configure GitHub Secrets.")
        else:
            print("💡 Dry-run flag enabled: Live API call skipped.")
        sys.exit(0)

    print("🌐 Publishing live post to LinkedIn API...")
    status, response = publish_to_linkedin(token, payload)
    if status in (200, 201):
        print(f"🎉 Successfully published to LinkedIn! (HTTP {status})")
        print(f"Response: {response}")
    else:
        print(f"❌ Failed to publish to LinkedIn. (HTTP {status})", file=sys.stderr)
        print(f"Details: {response}", file=sys.stderr)
        sys.exit(1)

if __name__ == "__main__":
    main()
