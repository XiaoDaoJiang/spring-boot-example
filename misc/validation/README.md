## 声明Bean约束

1. 字段约束
    ```java
   public class Car {

    @NotNull
    private String manufacturer;

    @AssertTrue
    private boolean isRegistered;

    public Car(String manufacturer, boolean isRegistered) {
        this.manufacturer = manufacturer;
        this.isRegistered = isRegistered;
    }

    //getters and setters...

   }

2. 属性约束
   > Java 中，字段（Field）和属性（Property）是两个不同的概念。 字段指的是类中的成员变量，即用于存储对象状态的变量。在 Java
   中，字段可以分为静态字段和实例字段两种类型，静态字段是类的静态成员变量，属于类的属性，实例字段是实例的成员变量，属于对象的属性。
   属性指的是通过 getter 和 setter 方法来访问类中的成员变量。在 Java 中，属性是一种用于访问字段的方法，通过 getter
   方法可以获取字段的值，通过 setter 方法可以修改字段的值。属性可以提供一些控制和保护字段的功能，例如对字段的访问权限进行控制、对字段进行校验等。
   在 Java 中，通常会将类的成员变量定义为私有的，通过 getter 和 setter
   方法来访问，从而实现对成员变量的控制和保护。这种做法也符合封装的原则，使得类的实现细节对外部隐藏，提高了代码的可维护性和可复用性。

   ```java
   public class Car {
   
       private String manufacturer;
   
       private boolean isRegistered;
   
       public Car(String manufacturer, boolean isRegistered) {
           this.manufacturer = manufacturer;
           this.isRegistered = isRegistered;
       }
   
       @NotNull
       public String getManufacturer() {
           return manufacturer;
       }
   
       public void setManufacturer(String manufacturer) {
           this.manufacturer = manufacturer;
       }
   
       @AssertTrue
       public boolean isRegistered() {
           return isRegistered;
       }
   
       public void setRegistered(boolean isRegistered) {
           this.isRegistered = isRegistered;
       }
   }
   ```

3. 容器元素约束
   > 可以直接在参数化类型的类型参数上指定约束：这些约束称为容器元素约束。例如，List<@NotNull String> 、Set<T>、Map、Optional或自定义容器类型，这要求在约束定义中通过
   @Target 指定 ElementType.TYPE_USE
   ```java
   public class Car {

    private Map<@NotNull Part, List<@NotNull Manufacturer>> partManufacturers =
            new HashMap<>();

    //...
   }
   ```

4. 类约束
   > 如果验证依赖于对象的多个属性之间的关联，则类级约束很有用。
   
   例 2.9 中的 Car 类“类级别约束”具有两个属性 seatCount 和 passengers ，应确保乘客列表的条目数不超过可用座位数。为此，在类级别添加了 @ValidPassengerCount 约束。该约束的验证者可以访问完整的 Car 对象，从而允许比较座位和乘客的数量。
    ```java
   @ValidPassengerCount
   public class Car {
   
       private int seatCount;
   
       private List<Person> passengers;
   
       //...
   }
   ```
5. 约束的继承
   >当一个类实现一个接口或扩展另一个类时，在超类型上声明的所有约束批注的应用方式与在类本身上指定的约束相同。
   如果 Car 是 RentalCar 实现的接口，则会聚合接口与实现的覆盖方法上的约束。
   ```java
   public class Car {

    private String manufacturer;

    @NotNull
    public String getManufacturer() {
        return manufacturer;
    }

    //...
   }
   public class RentalCar extends Car {

    private String rentalStation;

    @NotNull
    public String getRentalStation() {
        return rentalStation;
    }

    //...
   }
   ```
6. 对象级联验证
   > 对象图中的对象可以通过级联验证进行验证。这意味着验证器将验证对象图中的所有对象。例如，如果 Car 对象具有对 Person 对象的引用，则可以通过级联验证来验证 Person 对象。要启用级联验证，可以使用 @Valid 注释字段或属性。例如，Car 类可以如下所示进行扩展：
   ```java
   public class Car {

    @NotNull
    @Valid
    private Person driver;

    //...
   }

   /**
    * 容器元素的级联约束
    */
   public class Car {
      
         private List<@NotNull @Valid Person> passengers = new ArrayList<Person>();
      
         private Map<@Valid Part, List<@Valid Manufacturer>> partManufacturers = new HashMap<>();
      
         //...
       }
   ```
## 验证Bean约束
1. 获取 Validator 实例
   ```java
   ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
   validator = factory.getValidator();
   ```
2. 验证器方法
    > 验证器提供了三个方法来验证对象：validateValue()、validateProperty()和 validate()。这些方法的区别在于验证的对象类型。validateValue()方法用于验证单个属性，validateProperty()方法用于验证单个属性，validate()方法用于验证整个对象图。
   ```java
   Car car = new Car( null, true );

   Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);
   Set<ConstraintViolation<Car>> constraintViolations = validator.validateProperty(car,"manufacturer");
   Set<ConstraintViolation<Car>> constraintViolations = validator.validateValue(Car.class,"manufacturer",null);
   ```
   
# 声明和验证方法约束
## 声明方法约束
1. 调用方在调用方法或构造函数之前必须满足的前提条件（通过对可执行文件的参数应用约束）
2. 在方法或构造函数调用返回后向调用方保证的后置条件（通过对可执行文件的返回值应用约束）

### 方法参数约束
```java
public class RentalStation {

    public RentalStation(@NotNull String name) {
        //...
    }

    public void rentCar(
            @NotNull Customer customer,
            @NotNull @Future Date startDate,
            @Min(1) int durationInDays) {
        //...
    }
}
```
#### 交叉参数约束（对多个参数聚合约束）
   ```java
   public class Car {

      @LuggageCountMatchesPassengerCount(piecesOfLuggagePerPassenger = 2)
      public void load(List<Person> passengers, List<PieceOfLuggage> luggage) {
         //...
      }
    }
   ```
### 返回值约束
   ```java
   public class RentalStation {

      @ValidRentalStation
      public RentalStation() {
         //...
      }
   
      @NotNull
      @Size(min = 1)
      public List<@NotNull Customer> getCustomers() {
         //...
         return null;
      }
    }
   ```
### 方法的执行参数、返回值级联约束
   ```java
   public class Garage {

      @NotNull
      private String name;
   
      @Valid
      public Garage(String name) {
         this.name = name;
      }
   
      public boolean checkCar(@Valid @NotNull Car car) {
         //...
         return false;
      }
      
      public boolean checkCars(@NotNull List<@Valid Car> cars) {
         //...
         return false;
      }
}
   public class Car {
   
      @NotNull
      private String manufacturer;
   
      @NotNull
      @Size(min = 2, max = 14)
      private String licensePlate;
   
      public Car(String manufacturer, String licencePlate) {
         this.manufacturer = manufacturer;
         this.licensePlate = licencePlate;
      }
   
      //getters and setters ...
   }
   ```
## 验证方法约束
1. 获取ExecutableValidator实例
   ```java
   ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
   executableValidator = factory.getValidator().forExecutables();
   ```
2. ExecutableValidator 方法
   * validateParameters() 和 validateReturnValue() 用于方法验证
   * validateConstructorParameters() 和 validateConstructorReturnValue() 用于构造函数验证
### 内置方法约束
   ```java
   public class Car {
   
       @ParameterScriptAssert(lang = "groovy", script = "luggage.size() <= passengers.size() * 2")
       public void load(List<Person> passengers, List<PieceOfLuggage> luggage) {
           //...
       }
   }
   ```
# 插值约束错误信息
   ```java
   public class Car {

      @NotNull
      private String manufacturer;
   
      @Size(
              min = 2,
              max = 14,
              message = "The license plate '${validatedValue}' must be between {min} and {max} characters long"
      )
      private String licensePlate;
   
      @Min(
              value = 2,
              message = "There must be at least {value} seat${value > 1 ? 's' : ''}"
      )
      private int seatCount;
   
      @DecimalMax(
              value = "350",
              message = "The top speed ${formatter.format('%1$.2f', validatedValue)} is higher " +
                      "than {value}"
      )
      private double topSpeed;
   
      @DecimalMax(value = "100000", message = "Price must not be higher than ${value}")
      private BigDecimal price;
   
      public Car(
              String manufacturer,
              String licensePlate,
              int seatCount,
              double topSpeed,
              BigDecimal price) {
         this.manufacturer = manufacturer;
         this.licensePlate = licensePlate;
         this.seatCount = seatCount;
         this.topSpeed = topSpeed;
         this.price = price;
      }

   //getters and setters ...
    }
   ```
# 约束分组
* 组继承
```java
public interface RaceCarChecks extends Default {
}
```
* 定义组顺序
```java
import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({ Default.class, CarChecks.class, DriverChecks.class })
public interface OrderedChecks {
}
```
* 重新定义默认组(替换 Default.class)
```java
@GroupSequence({ RentalChecks.class, CarChecks.class, RentalCar.class })
public class RentalCar extends Car {
    @AssertFalse(message = "The car is currently rented out", groups = RentalChecks.class)
    private boolean rented;

    public RentalCar(String manufacturer, String licencePlate, int seatCount) {
        super( manufacturer, licencePlate, seatCount );
    }

    public boolean isRented() {
        return rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }
}
```
* 动态定义分组
   ```java
   public class RentalCarGroupSequenceProvider
           implements DefaultGroupSequenceProvider<RentalCar> {
   
       @Override
       public List<Class<?>> getValidationGroups(RentalCar car) {
           List<Class<?>> defaultGroupSequence = new ArrayList<Class<?>>();
           defaultGroupSequence.add( RentalCar.class );
   
           if ( car != null && !car.isRented() ) {
               defaultGroupSequence.add( CarChecks.class );
           }
   
           return defaultGroupSequence;
       }
   }
  
   @GroupSequenceProvider(RentalCarGroupSequenceProvider.class)
   public class RentalCar extends Car {
   
       @AssertFalse(message = "The car is currently rented out", groups = RentalChecks.class)
       private boolean rented;
   
       public RentalCar(String manufacturer, String licencePlate, int seatCount) {
           super( manufacturer, licencePlate, seatCount );
       }
   
       public boolean isRented() {
           return rented;
       }
   
       public void setRented(boolean rented) {
           this.rented = rented;
       }
   }
   ```
### 组转换
与@Valid 一起使用，在级联验证时，将验证组转换为其他组
```java
public class Driver {

   @NotNull
   private String name;

   @Min(
           value = 18,
           message = "You have to be 18 to drive a car",
           groups = DriverChecks.class
   )
   public int age;

   @AssertTrue(
           message = "You first have to pass the driving test",
           groups = DriverChecks.class
   )
   public boolean hasDrivingLicense;

   public Driver(String name) {
      this.name = name;
   }

   public void passedDrivingTest(boolean b) {
      hasDrivingLicense = b;
   }

   public int getAge() {
      return age;
   }

   public void setAge(int age) {
      this.age = age;
   }

   // getters and setters ...
}

@GroupSequence({ CarChecks.class, Car.class })
public class Car {

   @NotNull
   private String manufacturer;

   @NotNull
   @Size(min = 2, max = 14)
   private String licensePlate;

   @Min(2)
   private int seatCount;

   @AssertTrue(
           message = "The car has to pass the vehicle inspection first",
           groups = CarChecks.class
   )
   private boolean passedVehicleInspection;

   @Valid
   @ConvertGroup(from = Default.class, to = DriverChecks.class)
   private Driver driver;

   public Car(String manufacturer, String licencePlate, int seatCount) {
      this.manufacturer = manufacturer;
      this.licensePlate = licencePlate;
      this.seatCount = seatCount;
   }

   public boolean isPassedVehicleInspection() {
      return passedVehicleInspection;
   }

   public void setPassedVehicleInspection(boolean passedVehicleInspection) {
      this.passedVehicleInspection = passedVehicleInspection;
   }

   public Driver getDriver() {
      return driver;
   }

   public void setDriver(Driver driver) {
      this.driver = driver;
   }

   // getters and setters ...
}
```
# 自定义约束
1. 创建一个简单约束
   1. 创建约束注解
      ```java
         public enum CaseMode {
            UPPER,
            LOWER;
         }
         
         @Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE, TYPE_USE })
         @Retention(RUNTIME)
         @Constraint(validatedBy = CheckCaseValidator.class)
         @Documented
         @Repeatable(List.class)
         public @interface CheckCase {
         
             String message() default "{org.hibernate.validator.referenceguide.chapter06.CheckCase." +
                     "message}";
         
             Class<?>[] groups() default { };
         
             Class<? extends Payload>[] payload() default { };
         
             CaseMode value();
         
             @Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
             @Retention(RUNTIME)
             @Documented
             @interface List {
                 CheckCase[] value();
             }
         }
         
         public class Severity {
            public interface Info extends Payload {
            }
         
            public interface Error extends Payload {
            }
         }
         
         public class ContactDetails {
            @NotNull(message = "Name is mandatory", payload = Severity.Error.class)
            private String name;
         
            @NotNull(message = "Phone number not specified, but not mandatory",
            payload = Severity.Info.class)
            private String phoneNumber;
         
            // ...
         }
      ```
      > payload 自定义有效负载的示例可以是严重性的定义：现在，客户端可以在验证 ContactDetails 实例后使用 ConstraintViolation.getConstraintDescriptor().getPayload() 访问约束的严重性，并根据严重性调整其行为。
   2. 实现验证器
      ```java
      public class CheckCaseValidator implements ConstraintValidator<CheckCase, String> {

      private CaseMode caseMode;
   
      @Override
      public void initialize(CheckCase constraintAnnotation) {
      this.caseMode = constraintAnnotation.value();
      }
   
      @Override
      public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
         if ( object == null ) {
             return true;
         }
   
           if ( caseMode == CaseMode.UPPER ) {
               return object.equals( object.toUpperCase() );
           }
           else {
               return object.equals( object.toLowerCase() );
           }
        }
      }
      ```
      ```java
      public class CheckCaseValidator implements ConstraintValidator<CheckCase, String> {

      private CaseMode caseMode;

      @Override
      public void initialize(CheckCase constraintAnnotation) {
            this.caseMode = constraintAnnotation.value();
      }
   
      @Override
      public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
      if ( object == null ) {
      return true;
      }

        boolean isValid;
        if ( caseMode == CaseMode.UPPER ) {
            isValid = object.equals( object.toUpperCase() );
        }
        else {
            isValid = object.equals( object.toLowerCase() );
        }

            if ( !isValid ) {
                constraintContext.disableDefaultConstraintViolation();
                constraintContext.buildConstraintViolationWithTemplate(
                    "{org.hibernate.validator.referenceguide.chapter06." +
                    "constraintvalidatorcontext.CheckCase.message}"
                )
                .addConstraintViolation();
            }

            return isValid;
        }
      }
      ```
      
   3. 定义默认错误信息
      ```properties
        org.hibernate.validator.referenceguide.chapter06.CheckCase.message=Case mode must be {value}.
      ```

2. 类级约束
   ```java
   @Target({ TYPE, ANNOTATION_TYPE })
   @Retention(RUNTIME)
   @Constraint(validatedBy = { ValidPassengerCountValidator.class })
   @Documented
   public @interface ValidPassengerCount {
   
       String message() default "{org.hibernate.validator.referenceguide.chapter06.classlevel." +
               "ValidPassengerCount.message}";
   
       Class<?>[] groups() default { };
   
       Class<? extends Payload>[] payload() default { };
   }
   
   public class ValidPassengerCountValidator
        implements ConstraintValidator<ValidPassengerCount, Car> {

    @Override
    public void initialize(ValidPassengerCount constraintAnnotation) {
    }

    @Override
    public boolean isValid(Car car, ConstraintValidatorContext context) {
        if ( car == null ) {
            return true;
        }

        return car.getPassengers().size() <= car.getSeatCount();
    }
   }
   ```
   * 自定义属性路径
   ```java
   public class ValidPassengerCountValidator
        implements ConstraintValidator<ValidPassengerCount, Car> {

    @Override
    public void initialize(ValidPassengerCount constraintAnnotation) {
    }

    @Override
    public boolean isValid(Car car, ConstraintValidatorContext constraintValidatorContext) {
    if ( car == null ) {
     return true;
    }

        boolean isValid = car.getPassengers().size() <= car.getSeatCount();

        if ( !isValid ) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate( "{my.custom.template}" )
                    .addPropertyNode( "passengers" ).addConstraintViolation();
        }

        return isValid;
   }
   }
     ```
3. 跨参数约束
   1. 交叉参数约束
   ```java
   @Constraint(validatedBy = ConsistentDateParametersValidator.class)
   @Target({ METHOD, CONSTRUCTOR, ANNOTATION_TYPE })
   @Retention(RUNTIME)
   @Documented
   public @interface ConsistentDateParameters {
   
       String message() default "{org.hibernate.validator.referenceguide.chapter04." +
               "crossparameter.ConsistentDateParameters.message}";
   
       Class<?>[] groups() default { };
   
       Class<? extends Payload>[] payload() default { };
   }
   ```
   2. 交叉约束验证器
   ```java
   @SupportedValidationTarget(ValidationTarget.PARAMETERS)
   public class ConsistentDateParametersValidator implements
   ConstraintValidator<ConsistentDateParameters, Object[]> {
   
       @Override
       public void initialize(ConsistentDateParameters constraintAnnotation) {
       }
   
       @Override
       public boolean isValid(Object[] value, ConstraintValidatorContext context) {
           if ( value.length != 2 ) {
               throw new IllegalArgumentException( "Illegal method signature" );
           }
   
           //leave null-checking to @NotNull on individual parameters
           if ( value[0] == null || value[1] == null ) {
               return true;
           }
   
           if ( !( value[0] instanceof Date ) || !( value[1] instanceof Date ) ) {
               throw new IllegalArgumentException(
                       "Illegal method signature, expected two " +
                               "parameters of type Date."
               );
           }
   
           return ( (Date) value[0] ).before( (Date) value[1] );
       }
   }
   ```
   > 在具有参数和返回值的方法上声明此类约束时，无法确定预期的约束目标。因此，同时是泛型和跨参数的约束必须定义一个成员 validationAppliesTo() ，该成员允许约束用户指定约束的目标，如例 6.16 “泛型和交叉参数约束”所示。
   ```java
   @Constraint(validatedBy = {
           ScriptAssertObjectValidator.class,
           ScriptAssertParametersValidator.class
   })
   @Target({ TYPE, FIELD, PARAMETER, METHOD, CONSTRUCTOR, ANNOTATION_TYPE })
   @Retention(RUNTIME)
   @Documented
   public @interface ScriptAssert {
   
       String message() default "{org.hibernate.validator.referenceguide.chapter04." +
               "crossparameter.ScriptAssert.message}";
   
       Class<?>[] groups() default { };
   
       Class<? extends Payload>[] payload() default { };
   
       String script();
       // 默认值 IMPLICIT 允许在可能的情况下自动派生目标（例如，如果在字段或具有参数但没有返回值的方法上声明约束）。
       ConstraintTarget validationAppliesTo() default ConstraintTarget.IMPLICIT;
   }
   ```
   ```java
   public class Car{
      @ScriptAssert(script = "arg1.size() <= arg0", validationAppliesTo = ConstraintTarget.PARAMETERS)
      public Car buildCar(int seatCount, List<Passenger> passengers) {
         //...
         return null;
      }
   }
   ```
   
4. 约束组合
   > 要创建组合约束，只需使用其包含约束对约束声明进行批注。如果组合约束本身需要验证器，则在 @Constraint 注释中指定此验证器。对于不需要额外验证器（如 @ValidLicensePlate ）的组合约束，只需将 validatedBy() 设置为空数组即可。
   ```java
   @NotNull
   @Size(min = 2, max = 14)
   @CheckCase(CaseMode.UPPER)
   @Target({ METHOD, FIELD, ANNOTATION_TYPE, TYPE_USE })
   @Retention(RUNTIME)
   @Constraint(validatedBy = { })
   @Documented
   public @interface ValidLicensePlate {

       String message() default "{org.hibernate.validator.referenceguide.chapter06." +
               "constraintcomposition.ValidLicensePlate.message}";

       Class<?>[] groups() default { };

       Class<? extends Payload>[] payload() default { };
   }
   
   public class Car {

    @ValidLicensePlate
    private String licensePlate;

    //...
   }
   ```
   >验证 Car 实例时检索到的 ConstraintViolation 集将包含 @ValidLicensePlate 约束的每个违反的组合约束的条目。如果您更喜欢单个 ConstraintViolation 以防违反任何组合约束，则可以按如下方式使用 @ReportAsSingleViolation 元约束：
   ```java
   //...
   @ReportAsSingleViolation
   public @interface ValidLicensePlate {
   
       String message() default "{org.hibernate.validator.referenceguide.chapter06." +
               "constraintcomposition.reportassingle.ValidLicensePlate.message}";
   
       Class<?>[] groups() default { };
   
       Class<? extends Payload>[] payload() default { };
   }
   ```
   约束的布尔组合
    ```java
   @ConstraintComposition(OR)
   @Pattern(regexp = "[a-z]")
   @Size(min = 2, max = 3)
   @ReportAsSingleViolation
   @Target({ METHOD, FIELD })
   @Retention(RUNTIME)
   @Constraint(validatedBy = { })
   public @interface PatternOrSize {
   String message() default "{org.hibernate.validator.referenceguide.chapter11." +
   "booleancomposition.PatternOrSize.message}";
   
       Class<?>[] groups() default { };
   
       Class<? extends Payload>[] payload() default { };
   }
   ```
# 值提取