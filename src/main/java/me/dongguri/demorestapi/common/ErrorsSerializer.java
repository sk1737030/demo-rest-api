package me.dongguri.demorestapi.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;


/**
 * JsonSerializer
 * json으로 serializer<Type> 변환
 *
 */
@JsonComponent // ObjectMapper에 등록을 해준다.
public class ErrorsSerializer extends JsonSerializer<Errors> {

    @Override
    public void serialize(Errors errors, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        errors.getFieldErrors().stream().forEach(e -> {
            try {
                jsonGenerator.writeStartObject(); // Json 오브젝트 만들기 시작
                jsonGenerator.writeStringField("field", e.getField());
                jsonGenerator.writeStringField("objectName", e.getObjectName());
                jsonGenerator.writeStringField("code", e.getCode());
                jsonGenerator.writeStringField("defaultMessage", e.getDefaultMessage());
                
                Object rejectedValue = e.getRejectedValue();
                
                if(rejectedValue != null) {
                    jsonGenerator.writeStringField("rejectedValue", rejectedValue.toString());
                }

                jsonGenerator.writeEndObject(); // Json 오브젝트 만들기 종료 
                
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });


        // 글로벌 에러를 담음 3가지 밖에 없다
        errors.getGlobalErrors().forEach(e -> {
            try {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("objectName", e.getObjectName());
                jsonGenerator.writeStringField("code", e.getCode());
                jsonGenerator.writeStringField("defaultMessage", e.getDefaultMessage());


                jsonGenerator.writeEndObject();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        jsonGenerator.writeEndArray(); // 배열로
    }
}
