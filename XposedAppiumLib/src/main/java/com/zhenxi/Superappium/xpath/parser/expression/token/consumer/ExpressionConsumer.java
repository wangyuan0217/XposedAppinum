package com.zhenxi.Superappium.xpath.parser.expression.token.consumer;

import com.zhenxi.Superappium.xpath.parser.TokenQueue;
import com.zhenxi.Superappium.xpath.parser.expression.token.TokenConsumer;

public class ExpressionConsumer implements TokenConsumer {
    public String consume(TokenQueue tokenQueue) {
        if (tokenQueue.matches("(")) {
            return tokenQueue.chompBalanced('(', ')');
        }
        return null;
    }

    public int order() {
        return 0;
    }

    public String tokenType() {
        return "EXPRESSION";
    }
}

