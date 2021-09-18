
package cn.cyejing.dam.core.selector.match;

import cn.cyejing.dam.common.utils.AntPathMatcher;


public class AntMatch implements Match{

    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public boolean match(String rule, String val) {
        return matcher.match(rule, val);
    }
}
