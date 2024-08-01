package com.xiaodao.validation.first;

import com.xiaodao.validation.payload.MyAgeStrConstraint;
import com.xiaodao.validation.payload.Severity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class Car {

    @NotNull
    private String manufacturer;

    @Size(min = 2, max = 14)
    private String licensePlate;

    @Min(2)
    private int seatCount;

    public Car(String manufacturer, String licencePlate, int seatCount) {
        this.manufacturer = manufacturer;
        this.licensePlate = licencePlate;
        this.seatCount = seatCount;
    }


    /**
     * 属性检测是通过 JavaBeans 规范实现的。具体来说，框架会通过反射扫描类中的方法，并检测符合 JavaBeans 规范的 getter 方法。
     * 例如，布尔类型的 getter 方法应该以 is 开头，而非布尔类型的 getter 方法则以 get 开头。
     */


    @AssertTrue(message = "isValid不能为false")
    public boolean isValid() {
        return false;
    }


    /**
     * 自定义 bean 方法的约束校验时，方法的返回值必须与方法名的相匹配
     * boolean => isXxx()
     * other => getXxx()
     */
    @NotBlank(message = "getValidStr不能为空")
    public String getValidStr() {
        return null;
    }


    @NotNull(message = "getValidInteger不能为空")
    public Integer getValidInteger() {
        return null;
    }


    @Max(value = 20, message = "getValidInteger > {value}")
    public Integer getValidIntegerMax() {
        return 40;
    }


    /**
     * payload 属性在约束注解中主要用于传递元数据或上下文信息，供自定义校验器在执行校验时使用。
     */
    @MyAgeStrConstraint(payload = Severity.Error.class, max = 50)
    public String getValidCustomPayload() {
        return "60";
    }

    /**
     * payload 属性在约束注解中主要用于传递元数据或上下文信息，供自定义校验器在执行校验时使用。
     */
    @MyAgeStrConstraint(payload = Severity.Info.class, max = 10)
    public String getValidCustomPayloadInfo() {
        return "20";
    }

    @MyAgeStrConstraint(max = 5)
    private String limitYears;
}