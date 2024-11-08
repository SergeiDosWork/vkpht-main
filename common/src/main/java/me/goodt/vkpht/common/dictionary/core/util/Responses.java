package me.goodt.vkpht.common.dictionary.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Responses {

    public static ResponseEntity<Void> created(Object id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-entity-id", id.toString());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    public static ResponseEntity<Void> ok() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public static ResponseEntity<Void> forbidden() {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
