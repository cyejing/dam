package cn.cyejing.dam.core.selector.match;


public class LikeMatch implements Match {
    @Override
    public boolean match(String rule, String val) {
        if (val == null || rule == null) {
            return false;
        }
        return val.contains(rule);
    }
}
