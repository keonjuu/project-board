package com.Board.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;

public class MessageCodesResolver {


     org.springframework.validation.MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();
     @Test
     void MessageCodesResolverObject(){
         String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");

         for (String messageCode : messageCodes) {
             System.out.println("messageCode = " + messageCode);
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
             System.out.println("messageCode = " + messageCode);

         }

         Assertions.assertThat(messageCodes).containsExactly(
                 "required.item.itemName",
                 "required.itemName",
                 "required.java.lang.String",
                 "required");

     }


}
