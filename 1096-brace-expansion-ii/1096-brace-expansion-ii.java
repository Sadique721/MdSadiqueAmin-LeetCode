class Solution {
    private int idx;
    private String exp;

    public List<String> braceExpansionII(String expression) {
        exp = expression;
        idx = 0;
        return new ArrayList<>(parseExpression());
    }

    private TreeSet<String> parseExpression() {
        TreeSet<String> res = parseTerm();

        while (idx < exp.length() && exp.charAt(idx) == ',') {
            idx++;
            res.addAll(parseTerm());
        }
        return res;
    }

    private TreeSet<String> parseTerm() {
        TreeSet<String> res = new TreeSet<>();
        res.add("");

        while (idx < exp.length()) {
            char c = exp.charAt(idx);
            if (c == '}' || c == ',') break;

            TreeSet<String> next = parseFactor();
            res = concat(res, next);
        }
        return res;
    }

    private TreeSet<String> parseFactor() {
        TreeSet<String> res = new TreeSet<>();
        char c = exp.charAt(idx);

        if (Character.isLowerCase(c)) {
            res.add(String.valueOf(c));
            idx++;
        } else { // '{'
            idx++;
            res = parseExpression();
            idx++; // skip '}'
        }
        return res;
    }

    private TreeSet<String> concat(TreeSet<String> a, TreeSet<String> b) {
        TreeSet<String> res = new TreeSet<>();
        for (String x : a) {
            for (String y : b) {
                res.add(x + y);
            }
        }
        return res;
    }
}