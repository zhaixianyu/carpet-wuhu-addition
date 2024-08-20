package com.zxy.carpet_wh_addition.config;

public interface RuleObserver {
    static void onRuleChange(String option) {
        throw new AssertionError();
    }
}
