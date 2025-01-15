package com.winten.greenlight.db.domain;

/* Redis ZSet(SortedSet) 자료구조는 key, value, score 로 구성되어 있다.
*
* String key: Table에 해당함
* String score: 순번(seq)에 해당함
* double value: 상세값에 해당함 (userId, username 등등)
* */
public interface BaseZSetEntity {
    String key();
    String value();
    double score();
}
