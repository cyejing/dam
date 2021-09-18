
package cn.cyejing.dam.common.expression.token;


public interface TokenReader {

    boolean check(TokenStream tokenStream);

    void process(TokenStream tokenStream);

}
