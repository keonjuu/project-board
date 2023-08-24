package com.Board.validation;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;

@Slf4j
public class MessageCodesResolver {


     org.springframework.validation.MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();
     @Test
     void MessageCodesResolverObject(){
         String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");

         for (String messageCode : messageCodes) {
             log.info("messageCode = {}" + messageCode);
         }
/*
         messageCode = required.item
         messageCode = required
*/
         Assertions.assertThat(messageCodes).containsExactly("required.item", "required");
     }

     @Test
    void messageCodesResolverField(){
         String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
         for (String messageCode : messageCodes) {
             log.info("messageCode = {}" + messageCode);

         }

         Assertions.assertThat(messageCodes).containsExactly(
                 "required.item.itemName",
                 "required.itemName",
                 "required.java.lang.String",
                 "required");

     }


}
