package me.dongguri.demorestapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;

@Component
public class EventValidator {
    public void validate(EventDto eventDto, BindingResult errors) {
        if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0) {
            errors.rejectValue("basePrice", "wrongValue", "BasePrice is Wrong"); // fieldㅇ에러
         //   errors.rejectValue("maxPrice", "wrongValue", "MaxPrice is Wrong"); //field에러
            errors.reject("wrongPrices","Values of Prices are wrong"); //global Error로 들어감
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();

        if(endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
        endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
        endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
            errors.rejectValue("endEventDateTime", "wrongValue" , "EndEventDateTImeIsWrong");
        }
    }
}
