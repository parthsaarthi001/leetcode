public class Solution {
    List<String> ret;
    private static final String[] ops = new String[]{"+", "-", "*"};

    private boolean checkValue(List<String> express, int target) {
        for (int i = 0; i < express.size(); ++i) {
            if (i % 2 == 0) {
                try {
                    Integer.parseInt(express.get(i));
                } catch (NumberFormatException e) {
                    return false;
                }
            } else {
                if (express.get(i) != "+" && express.get(i) != "-" && express.get(i) != "*")
                    return false;
            }
        }

        // * first
        Stack<String> stack = new Stack<>();
        for (int i = 0; i < express.size(); ++i) {
            if (!express.get(i).equals("*")) stack.push(express.get(i));
            else {
                int prod = Integer.parseInt(express.get(i + 1)) * Integer.parseInt(stack.pop());
                stack.push(String.valueOf(prod));
                i++;
            }
        }

        // + and -
        List<String> addSub = new LinkedList<>();
        while (!stack.empty()) addSub.add(0, stack.pop());
        int val = Integer.parseInt(addSub.get(0));
        for (int i = 1; i < addSub.size(); ++i) {
            if (addSub.get(i).equals("+"))
                val = val + Integer.parseInt(addSub.get(i + 1));
            else
                val = val - Integer.parseInt(addSub.get(i + 1));
            i++;
        }
        return val == target;
    }

    private String getExpress(List<String> express) {
        StringBuilder sb = new StringBuilder();
        for (String str : express) sb.append(str);
        return sb.toString();
    }

    private void dfs(String num, int start, List<String> express, StringBuilder cur, int target) {
        if (start == num.length()) {
            if (cur.length() != 0) express.add(cur.toString());
            if (checkValue(express, target)) ret.add(getExpress(express));
            if (cur.length() != 0) express.remove(express.size() - 1);
            return;
        }

        // add num
        if (cur.length() == 0 || (cur.length() > 0 && cur.charAt(0) != '0')) {
            cur.append(num.charAt(start));
            dfs(num, start + 1, express, cur, target);
            cur.deleteCharAt(cur.length() - 1);
        }

        if (cur.length() != 0) {
            // add ops
            express.add(cur.toString());
            for (int i = 0; i < 3; ++i) {
                express.add(ops[i]);
                dfs(num, start, express, new StringBuilder(), target);
                express.remove(express.size() - 1);
            }
            express.remove(express.size() - 1);
        }
    }

    public List<String> addOperators(String num, int target) {
        if(target == Integer.MIN_VALUE) return Collections.emptyList();
        if(num == null || num.length() == 0) return Collections.emptyList();
        ret = new ArrayList<>();
        dfs(num, 0, new ArrayList<>(), new StringBuilder(), target);
        return ret;
    }
}


/////////////////////////////////////
class Solution {
    private void dfs(String num, int index, int target, StringBuilder sb, int preValue, int preEle, List<String> ret) {
        if (index == num.length()) {
            if (preValue == target) ret.add(sb.toString());
            return;
        }

        int len = sb.length();
        for (int i = index + 1; i <= num.length(); ++i) {
            String n = num.substring(index, i);
            if (n.length() > 1 && n.charAt(0) == '0') continue;
            int cur = 0;
            try {
                cur = Integer.parseInt(n);
            } catch (NumberFormatException e ) {
                continue;
            }
           
            if (len == 0) {
                sb.append(cur);
                dfs(num, i, target, sb, cur, cur, ret);
                sb.setLength(len);
            } else {
                //add
                sb.append("+").append(cur);
                dfs(num, i, target, sb, preValue + cur, cur, ret);
                sb.setLength(len);

                // substract
                sb.append("-").append(cur);
                dfs(num, i, target, sb, preValue - cur, -cur, ret);
                sb.setLength(len);

                // multiple
                sb.append("*").append(cur);
                dfs(num, i, target, sb, preValue - preEle + preEle * cur, preEle * cur, ret);
                sb.setLength(len);
            }
        }
    }

    public List<String> addOperators(String num, int target) {
        List<String> ret = new ArrayList<>();
        dfs(num, 0, target, new StringBuilder(), 0, 0, ret);
        return ret;
    }
}