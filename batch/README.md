## 批处理场景

### 数据库用户数据加密

需要对数据库中存储了用户数据的一些表中字段进行加密，这些数据数据量大，而且存储在不同的表中，需要从数据库读取出来，在程序中进行加密处理再写回数据库

### 数据迁移

对外部系统的数据进行导入，大部分是离线进行（excel文件）

## 难点与解决

大数据：应用停机维护，通过利用现代多核CPU，分布式集群分片的思想，提高任务的并行度，提高数据的处理能力
作业时间长
无法监控（出现错误，难以感知）
补偿逻辑

### MapStruct 使用

MapStruct 是一个用于 Java Bean 类的类型安全且高效的映射器生成器，通过 Java
注解处理器生成。它可以帮助开发者免于手动编写映射代码，这是一项繁琐且容易出错的任务。MapStruct
提供了合理的默认设置和许多内置的类型转换，但在配置或实现特殊行为时，它不会干扰你的代码。

与运行时映射框架相比，MapStruct 具有以下优势：

- **快速执行**：通过使用普通的方法调用代替反射。
- **编译时的类型安全**：只有能够相互映射的对象和属性才会被映射，避免了如将订单实体错误映射到客户 DTO 的情况。
- **自包含的代码**：没有运行时依赖。
- **清晰的编译时错误报告**：
    - 映射不完整（并非所有目标属性都被映射）。
    - 映射不正确（找不到合适的映射方法或类型转换）。
- **易于调试的映射代码**：在生成器中出现错误时，代码可以手动编辑。

要在两种类型之间创建映射，可以像下面这样声明一个映射器接口：

```java

@Mapper
public interface CarMapper {

    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    @Mapping(target = "seatCount", source = "numberOfSeats")
    CarDto carToCarDto(Car car);
}

```

在编译时，MapStruct 将生成该接口的实现。生成的实现使用普通的 Java
方法调用在源对象和目标对象之间进行映射，即不涉及反射。默认情况下，如果源和目标中的属性名称相同，则它们会被映射，但你可以使用
@Mapping 和其他一些注解来控制这一点以及许多其他方面的行为。

#### install

```xml
...
<properties>
    <org.mapstruct.version>1.5.5.Final</org.mapstruct.version>
</properties>
        ...
<dependencies>
<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>${org.mapstruct.version}</version>
</dependency>
</dependencies>
        ...
<build>
<plugins>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.8.1</version>
        <configuration>
            <source>1.8</source>
            <target>1.8</target>
            <annotationProcessorPaths>
                <path>
                    <groupId>org.mapstruct</groupId>
                    <artifactId>mapstruct-processor</artifactId>
                    <version>${org.mapstruct.version}</version>
                </path>
            </annotationProcessorPaths>
        </configuration>
    </plugin>
</plugins>
</build>
```

### 配置

#### 与 Lombok 一起使用
Lombok 1.18.16 引入了一项重大更改（变更日志）。必须添加附加注释处理器lombok-mapstruct-binding ( Maven )，否则 MapStruct 将停止与 Lombok 配合使用。这解决了 Lombok 和 MapStruct 模块的编译问题。

```xml

<properties>
    <org.mapstruct.version>1.5.5.Final</org.mapstruct.version>
    <org.projectlombok.version>1.18.30</org.projectlombok.version>
    <lombok-mapstruct-binding.version>0.2.0</lombok-mapstruct-binding.version>
</properties>

<build>
<pluginManagement>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.1</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
                <!-- See https://maven.apache.org/plugins/maven-compiler-plugin/compile-mojo.html -->
                <!-- Classpath elements to supply as annotation processor path. If specified, the compiler   -->
                <!-- will detect annotation processors only in those classpath elements. If omitted, the     -->
                <!-- default classpath is used to detect annotation processors. The detection itself depends -->
                <!-- on the configuration of annotationProcessors.                                           -->
                <!--                                                                                         -->
                <!-- According to this documentation, the provided dependency processor is not considered!   -->
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.mapstruct</groupId>
                        <artifactId>mapstruct-processor</artifactId>
                        <version>${org.mapstruct.version}</version>
                    </path>
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                        <version>${org.projectlombok.version}</version>
                    </path>
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok-mapstruct-binding</artifactId>
                        <version>${lombok-mapstruct-binding.version}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</pluginManagement>
</build>
```

### 定义映射器

#### 基本映射
* 一对一映射
* 多对一映射
* map to bean
* 集合映射
* 隐式类型转换
* 更新现有的 bean 实例
* 常量，表达式映射，空检查，NULL默认值，忽略映射



```java

@Mapper
public interface CarMapper {

    @Mapping(target = "manufacturer", source = "make")
    @Mapping(target = "seatCount", source = "numberOfSeats")
    CarDto carToCarDto(Car car);

    @Mapping(target = "fullName", source = "name")
    PersonDto personToPersonDto(Person person);
}
```

#### 映射更新现有的 bean 实例

```java

@Mapper
public interface CarMapper {

    void updateCarFromDto(CarDto carDto, @MappingTarget Car car);
}
```

### 获取映射器

* Mappers 工厂（无依赖注入）
  通过 Mappers.getMapper(Class) 方法获取映射器实例
  ```java
    CarMapper carMapper = Mappers.getMapper(CarMapper.class);
  ```
  > 实现原理
  > 1. 通过 @Mapper 注解，MapStruct 会生成一个实现了 CarMapper 接口的类
  > 2. 通过 Mappers.getMapper(CarMapper.class) 方法获取映射器实例：反射实例化实现类 carMapperImpl
    ```java
    // org.mapstruct.factory.Mappers.doGetMapper
    private static <T> T doGetMapper(Class<T> clazz, ClassLoader classLoader) throws NoSuchMethodException {
        try {
            @SuppressWarnings( "unchecked" )
            Class<T> implementation = (Class<T>) classLoader.loadClass( clazz.getName() + IMPLEMENTATION_SUFFIX );
            Constructor<T> constructor = implementation.getDeclaredConstructor();
            constructor.setAccessible( true );
  
            return constructor.newInstance();
        }
        catch (ClassNotFoundException e) {
            return getMapperFromServiceLoader( clazz, classLoader );
        }
        catch ( InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException( e );
        }
    }
    ```

* 通过 @Mapper(componentModel = "spring") 注解，将映射器实例注入到 Spring 容器中，使用时直接注入
  ```java
  @Autowired
  private CarMapper carMapper;
  ```

#### 映射

#### 多个源参数的映射

```java

@Mapper
public interface AddressMapper {

    @Mapping(target = "description", source = "person.description")
    @Mapping(target = "houseNumber", source = "address.houseNo")
    DeliveryAddressDto personAndAddressToDeliveryAddressDto(Person person, Address address);

    @Mapping(target = "description", source = "person.description")
    @Mapping(target = "houseNumber", source = "hn")
    DeliveryAddressDto personAndAddressToDeliveryAddressDto(Person person, Integer hn);
}

```

### 重用映射配置
1. 映射组合，使用自定义注解与需要重用的映射@Mapping注解一起使用
    ```java
    
    @Retention(RetentionPolicy.CLASS)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", expression = "java(new java.util.Date())")
    @Mapping(target = "name", source = "groupName")
    public @interface ToEntity {
    }
    
    
    @Mapper
    public interface StorageMapper {
    
        StorageMapper INSTANCE = Mappers.getMapper(StorageMapper.class);
    
        // @Mapping(target = "id", ignore = true)
        // @Mapping(target = "creationDate", expression = "java(new java.util.Date())")
        // @Mapping(target = "name", source = "groupName")
        @ToEntity
        @Mapping(target = "weightLimit", source = "maxWeight")
        ShelveEntity map(ShelveDto source);
    
        // @Mapping(target = "id", ignore = true)
        // @Mapping(target = "creationDate", expression = "java(new java.util.Date())")
        // @Mapping(target = "name", source = "groupName")
        @ToEntity
        @Mapping(target = "label", source = "designation")
        BoxEntity map(BoxDto source);
    }
    ```
2. 使用 @InheritConfiguration/@InheritInverseConfiguration 注解，自动继承
   方法级配置注释（例如@Mapping 、 @BeanMapping 、 @IterableMapping等）可以使用注释@InheritConfiguration从一个映射方法继承到类似的方法：
   ```java
    @Mapper
    public interface CarMapper {

        @Mapping(target = "numberOfSeats", source = "seatCount")
        Car carDtoToCar(CarDto car);

        // Inherit the configuration from carDtoToCar
        @InheritConfiguration
        void carDtoIntoCar(CarDto carDto, @MappingTarget Car car);
    }
      ```
   如果有多个方法可用作继承源，则必须在注释中指定方法名称： @InheritConfiguration( name = "carDtoToCar" )
   在双向映射的情况下，例如从实体到DTO以及从DTO到实体，正向方法和反向方法的映射规则通常是相似的，并且可以简单地通过切换source和target来反转。
   使用@InheritInverseConfiguration注解来指示一个方法应该继承相应反向方法的反向配置。
   继承的映射配置可以另外应用@Mapping 、 @BeanMapping等来覆盖或修改配置。
    ```java
     @Mapper
     public interface CarMapper {
    
          @Mapping(target = "numberOfSeats", source = "seatCount")
          Car carDtoToCar(CarDto car);
    
          @InheritInverseConfiguration
          @Mapping(target = "numberOfSeats", ignore = true) // override the inherited configuration
          void carIntoCarDto(Car car, @MappingTarget CarDto carDto);
     }
    ```
3.  共享配置，通过定义@MapperConfig注解的接口，然后在映射器接口上使用@Mapper(config = CentralConfig.class)引用
    ```java
    @MapperConfig(
        uses = CustomMapperViaMapperConfig.class,
        unmappedTargetPolicy = ReportingPolicy.ERROR
    )
    public interface CentralConfig {
        // Not intended to be generated, but to carry inheritable mapping annotations:
        @Mapping(target = "primaryKey", source = "technicalKey")
        BaseEntity anyDtoToEntity(BaseDto dto);
    }
    
    @Mapper(config = CentralConfig.class, uses = { CustomMapperViaMapper.class } )
    // Effective configuration:
    // @Mapper(
    //     uses = { CustomMapperViaMapper.class, CustomMapperViaMapperConfig.class },
    //     unmappedTargetPolicy = ReportingPolicy.ERROR
    // )
    public interface SourceTargetMapper {
        @Mapping(target = "numberOfSeats", source = "seatCount")
        // additionally inherited from CentralConfig, because Car extends BaseEntity and CarDto extends BaseDto:
        // @Mapping(target = "primaryKey", source = "technicalKey")
        Car toCar(CarDto car);
    }
    ```
    可以通过 @MapperConfig#mappingInheritanceStrategy() 来控制继承策略，用来确定继承配置是否会实现类似 @InheritConfiguration/@InheritInverseConfiguration
中方法级别的继承
    * EXPLICIT：默认值，只有在显式指定了@InheritConfiguration/@InheritInverseConfiguration时才会继承配置
    * AUTO_INHERIT_FROM_CONFIG：从配置接口继承配置
    * AUTO_INHERIT_REVERSE_CONFIG：从配置接口的反向配置继承配置
    * AUTO_INHERIT_ALL_FROM_CONFIG：从配置接口和反向配置继承配置


### 自定义映射

#### 映射器中的自定义映射方法

通过在映射器（接口或者抽象类）中定义的自定义方法实现自定义映射逻辑，以下代码假设 CarDto/Car 中有一个类型为 PersonDto/Person
的属性 driver 需要自定义映射
> MapStruct 将生成CarMapper的子类，其中包含carToCarDto()方法的实现，因为该方法被声明为抽象。 carToCarDto()
> 中生成的代码将在映射driver属性时调用手动实现的personToPersonDto()方法

```java

@Mapper
public interface CarMapper {

    @Mapping(...)
    ...

    CarDto carToCarDto(Car car);

    default PersonDto personToPersonDto(Person person) {
        //hand-written mapping logic
    }
}

@Mapper
public abstract class CarMapper {

    @Mapping(...)
    ...

    public abstract CarDto carToCarDto(Car car);

    public PersonDto personToPersonDto(Person person) {
        //hand-written mapping logic
    }
}
```

#### 调用其他的映射器

MapStruct 还可以调用其他类中定义的映射方法，无论是由 MapStruct
生成的映射器还是手写的映射方法。这对于在多个类中构建映射代码（例如，每个应用程序模块使用一种映射器类型）或者如果您想要提供
MapStruct 无法生成的自定义映射逻辑时非常有用。
假设 CarDto/Car 中有一个不同类型的属性 manufacturingDate，通过 DateMapper 类中的 asString() 和 asDate() 方法进行转换

1. 手动实现的映射器类
    ```java
    public class DateMapper {
    
        public String asString(Date date) {
            return date != null ? new SimpleDateFormat( "yyyy-MM-dd" )
                .format( date ) : null;
        }
    
        public Date asDate(String date) {
            try {
                return date != null ? new SimpleDateFormat( "yyyy-MM-dd" )
                    .parse( date ) : null;
            }
            catch ( ParseException e ) {
                throw new RuntimeException( e );
            }
        }
    }
    ```
2. 引用另一个映射器类
    ```java
    @Mapper(uses = DateMapper.class)
    public interface CarMapper {
    
        CarDto carToCarDto(Car car);
    }
    ```

#### 使用限定符的映射器
当具有相同参数与返回类型的多个映射方法时
1. 使用 @Named 注解为映射方法命名，以便在 @Mapping 注解中引用（使用 qualifiedByName）
   ```java
    
    @Named("TitleTranslator")
    public class Titles {
    
        @Named("EnglishToGerman")
        public String translateTitleEG(String title) {
            // some mapping logic
        }
    
        @Named("GermanToEnglish")
        public String translateTitleGE(String title) {
            // some mapping logic
        }
    }
    
    @Mapper( uses = Titles.class )
    public interface MovieMapper {
    
    @Mapping( target = "title", qualifiedByName = { "TitleTranslator", "EnglishToGerman" } )
    GermanRelease toGerman( OriginalRelease movies );
    
    }
    ```
2. 使用限定符 @Qualifier 注解，以便在 @Mapping 注解中引用（使用 qualifiedBy）
    ```java
    import org.mapstruct.Qualifier;
    
    @Qualifier
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.CLASS)
    public @interface TitleTranslator {
    }
    
    @Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    public @interface EnglishToGerman {
    }
    
    @Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    public @interface GermanToEnglish {
    }
    
    
    @TitleTranslator
    public class Titles {
    
        @EnglishToGerman
        public String translateTitleEG(String title) {
            // some mapping logic
        }
    
        @GermanToEnglish
        public String translateTitleGE(String title) {
            // some mapping logic
        }
    }
    
    @Mapper( uses = Titles.class )
    public interface MovieMapper {
    
         @Mapping( target = "title", qualifiedBy = { TitleTranslator.class, EnglishToGerman.class } )
         GermanRelease toGerman( OriginalRelease movies );
    
    }
    ```


#### 传递类型参数给自定义映射器的映射（用于完成特定逻辑）

```java

@ApplicationScoped // CDI component model
public class ReferenceMapper {

    @PersistenceContext
    private EntityManager entityManager;

    public <T extends BaseEntity> T resolve(Reference reference, @TargetType Class<T> entityClass) {
        return reference != null ? entityManager.find(entityClass, reference.getPk()) : null;
    }

    public Reference toReference(BaseEntity entity) {
        return entity != null ? new Reference(entity.getPk()) : null;
    }
}

@Mapper(componentModel = MappingConstants.ComponentModel.CDI, uses = ReferenceMapper.class)
public interface CarMapper {

    Car carDtoToCar(CarDto carDto);
}
```

生成的代码

```java
//GENERATED CODE
@ApplicationScoped
public class CarMapperImpl implements CarMapper {

    @Inject
    private ReferenceMapper referenceMapper;

    @Override
    public Car carDtoToCar(CarDto carDto) {
        if (carDto == null) {
            return null;
        }

        Car car = new Car();

        car.setOwner(referenceMapper.resolve(carDto.getOwner(), Owner.class));
        // ...

        return car;
    }
}
```

### 传递上下文或状态参数给自定义方法
其他上下文或状态信息可以通过生成的映射方法传递到带有@Context参数的自定义方法。如果适用，此类参数会传递给其他映射方法、 @ObjectFactory方法（请参阅对象工厂）或@BeforeMapping / @AfterMapping方法（请参阅使用 before-mapping 和 after-mapping 方法进行映射自定义），因此可以在自定义代码中使用。

```java
public abstract CarDto toCar(Car car, @Context Locale translationLocale);

protected OwnerManualDto translateOwnerManual(OwnerManual ownerManual, @Context Locale locale) {
    // manually implemented logic to translate the OwnerManual with the given Locale
}

// GENERATED CODE
public CarDto toCar(Car car, Locale translationLocale) {
    if (car == null) {
        return null;
    }

    CarDto carDto = new CarDto();

    carDto.setOwnerManual(translateOwnerManual(car.getOwnerManual(), translationLocale);
    // more generated mapping code

    return carDto;
}
```

### 高级自定义映射器
1. 通过装饰器，将自定义映射逻辑定义在装饰器类上，增强映射器的功能
   实际使用有两种方式
   1. 在映射器接口上使用 @DecoratedWith 注解，指定装饰器类
      ```java
         @Mapper
         @DeWith(CarMapperDecorator.class)
         public interface CarMapper {

             CarDto carToCarDto(Car car);
         }

         public class CarMapperDecorator implements CarMapper {

             private final CarMapper delegate;

             public CarMapperDecorator(CarMapper delegate) {
                 this.delegate = delegate;
             }

             @Override
             public CarDto carToCarDto(Car car) {
                 CarDto dto = delegate.carToCarDto( car );
                 // custom mapping logic
                 return dto;
             }
         }
         ```
   2. 使用 Spring 组件模型的装饰器
      ```java
      public abstract class PersonMapperDecorator implements PersonMapper {
    
        @Autowired
        @Qualifier("delegate")
        private PersonMapper delegate;
    
        @Override
        public PersonDto personToPersonDto(Person person) {
            PersonDto dto = delegate.personToPersonDto( person );
            dto.setName( person.getFirstName() + " " + person.getLastName() );
             return dto;
        }
       }
      
        @Autowired
        private PersonMapper personMapper; // injects the decorator, with the injected original mapper

      ```
2. 使用 @ObjectFactory 注解，自定义创建目标对象的逻辑
3. 使用 @BeforeMapping 和 @AfterMapping 注解，自定义映射前后的逻辑（可以接受上下文@Context、@MappingTarget、@TargetType参数）
   使用 @BeforeMapping 和 @AfterMapping 能够实现对当前mapper或者作为更多mapper的父类，实现更通用的逻辑

    ```java
    @Mapper
    public abstract class VehicleMapper {
    
        @BeforeMapping
        protected void flushEntity(AbstractVehicle vehicle) {
            // I would call my entity manager's flush() method here to make sure my entity
            // is populated with the right @Version before I let it map into the DTO
        }
    
        @AfterMapping
        protected void fillTank(AbstractVehicle vehicle, @MappingTarget AbstractVehicleDto result) {
            result.fuelUp( new Fuel( vehicle.getTankCapacity(), vehicle.getFuelType() ) );
        }
    
        public abstract CarDto toCarDto(Car car);
    }
    
    // Generates something like this:
    public class VehicleMapperImpl extends VehicleMapper {
    
        public CarDto toCarDto(Car car) {
            flushEntity( car );
    
            if ( car == null ) {
                return null;
            }
    
            CarDto carDto = new CarDto();
            // attributes mapping ...
    
            fillTank( car, carDto );
    
            return carDto;
        }
    }
    ```

### 使用技巧
1. 当使用dto更新已有entity时，如何避免null值覆盖已有值
   ```java
   @Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
   public interface CarMapper {
   
       void updateCarFromDto(CarDto carDto, @MappingTarget Car car);
   }
   ```
2. 当使用dto更新已有entity时，如何避免目标非null值被dto源值覆盖
   先将目标entity的非null值拷贝到dto中，最后将dto更新到entity，这样可以保留目标entity的非null值
   ```java
   @Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
   public interface CarMapper {

        void updateDtoFromCar(Car car, @MappingTarget CarDto carDto);
   
        void updateCarFromDto(CarDto carDto, @MappingTarget Car car);
   
        default void mergeCarFromDto(CarDto carDto, @MappingTarget Car car){
            this.updateDtoFromCar(car, carDto);
            this.updateCarFromDto(carDto, car);
        }
   }
   ```
3. MapStruct 非常智能，能够自由组合、继承或实现的相关自定义映射，同时能够自动寻找合适的映射方法

## MapStruct Spring Extension
MapStruct Spring Extensions 是一个 Java注释处理器，它使用Spring 框架特有的功能扩展了众所周知的MapStruct 项目。
您所要做的就是定义 MapStruct 映射器来扩展 Spring 的Converter 接口。在编译期间，扩展将生成一个适配器，该适配器允许标准 MapStruct 映射器使用 Spring 的ConversionService 。
这使得开发人员能够在其uses属性中仅使用ConversionService来定义 MapStruct 映射器，而不必单独导入每个映射器，从而允许映射器之间的松散耦合。

### 使用
MapStruct Spring Extensions 是一个基于JSR 269的 Java 注释处理器，因此可以在命令行构建（javac、Ant、Maven 等）以及 IDE 中使用。最低 JDK 版本为 11。
此外，您的项目中还需要 MapStruct 本身（至少版本1.4.0.Final ）。

### 注意事项
使用 spring-boot-starter-web 依赖会自动注册所有的 converter ConversionService ，其他情况需要手动添加
例如测试时，使用 @ConverterScan 通过导入 @Import(ConverterRegistrationConfiguration.class)

```java
@ExtendWith(SpringExtension.class)
class ConversionServiceAdapterIntegrationTest {
    @Configuration
    @ConverterScan(basePackageClasses = MapperSpringConfig.class)
    static class ScanConfiguration {
    }
}

/**
 * 使用注入的ConfigurableConversionService注册所有可用的Converters 。
 * 标准 Spring Boot 项目将自动执行此操作，因此大多数生产代码无需明确包含此类。它适用于测试和应用程序上下文包含多个ConversionService bean 的情况。
 */
@Configuration
class ConverterRegistrationConfiguration {

    @Bean
    ConversionService basicConversionService(final List<Converter<?, ?>> converters) {
        final var conversionService = new DefaultConversionService();
        converters.forEach(conversionService::addConverter);
        return conversionService;
    }
}
```

当不想改变应用程序中默认 ConverterService（mvcConversionService） 时，需要手动创建一个自定义的 ConverterService bean，比如：myConversionService
```java
@SpringMapperConfig(conversionServiceAdapterClassName = "MyAdapter", conversionServiceBeanName = "myConversionService")
public interface MapperSpringConfig {
}

@Configuration
public class ConverterConfig {

    @Bean
    ConfigurableConversionService myConversionService() {
        return new DefaultConversionService();
    }
}

```
但是这样，必须手动增加转换器
```java
    void addMappersToConversionService() {
    conversionService.addConverter(carMapper);
    conversionService.addConverter(seatConfigurationMapper);
    conversionService.addConverter(wheelMapper);
    conversionService.addConverter(wheelsMapper);
    conversionService.addConverter(wheelsDtoListMapper);
}
```

也可以通过将属性generateConverterScan设置为true将在项目内创建一个替代方案。需要注意的是：此版本不会使用给定的 bean 名称创建ConversionService ，而只是使用给定名称标识的 bean 注册所有映射器。
会在target/generated-sources/对应包下自动生成如下源文件：
* ConverterRegistrationConfiguration.java
```java
@Configuration
class ConverterRegistrationConfiguration {
  private final ConfigurableConversionService myConversionService;

  private final List<Converter<?, ?>> converters;

  // 完成对手动创建的 myConversionService bean 的加载映射器
  ConverterRegistrationConfiguration(
      @Qualifier("myConversionService") final ConfigurableConversionService myConversionService,
      final List<Converter<?, ?>> converters) {
    this.myConversionService = myConversionService;
    this.converters = converters;
  }

  @PostConstruct
  void registerConverters() {
    converters.forEach(myConversionService::addConverter);
  }
}
```
* ConverterScan.java
* ConverterScans.java

但是无法用于测试（非@SpringBootTest）使用
* 与测试扩展版本不同，该版本非常适合在生产代码中使用。
* 在测试中，开发人员仍然需要自己提供ConfigurableConversionService ，例如：
```java
@ExtendWith(SpringExtension.class)
public class ConversionServiceAdapterIntegrationTest {
  @Configuration
  @ConverterScan
  static class AdditionalBeanConfiguration {
    @Bean
    ConfigurableConversionService myConversionService() {
      return new DefaultConversionService();
    }
  }

  @Autowired
  @Qualifier("myConversionService")
  private ConversionService conversionService;
}
```
由于Spring Converter 的设计，每个converter都有唯一的sourceType,targetType，通过ConverterService统一完成convert，意味着虽然不必单独导入每个映射器，从而允许映射器之间的松散耦合
但是也导致随着业务的复杂，将会产生大量的专用converter；
避免mapstruct 使用 uses，所有这一切都可以通过 MapStruct 的核心功能来实现。但是，当一个 Mapper 想要调用另一个 Mapper 时，它无法通过ConversionService获取路由，
因为后者的convert方法与 MapStruct 期望的映射方法的签名不匹配。
因此，开发人员仍然必须将每个调用的映射器添加到调用映射器的uses元素中。这会在映射器之间创建（除了可能很长的列表之外） ConversionService旨在避免的紧密耦合。
这就是 MapStruct Spring 扩展可以提供帮助的地方。在构建中包含这两个工件将生成一个可供调用映射器使用的适配器类。假设上面的 CarMapper 附带了一个 SeatConfigurationMapper：
```java
@Mapper
public interface SeatConfigurationMapper extends Converter<SeatConfiguration, SeatConfigurationDto> {
    @Mapping(target = "seatCount", source = "numberOfSeats")
    @Mapping(target = "material", source = "seatMaterial")
    SeatConfigurationDto convert(SeatConfiguration seatConfiguration);
}
```
生成的 Adapter 类将如下所示：
```java
@Component
public class ConversionServiceAdapter {
  private final ConversionService conversionService;

  public ConversionServiceAdapter(@Lazy final ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  public CarDto mapCarToCarDto(final Car source) {
    return (CarDto) conversionService.convert(source, TypeDescriptor.valueOf(Car.class), TypeDescriptor.valueOf(CarDto.class));
  }

  public SeatConfigurationDto mapSeatConfigurationToSeatConfigurationDto(
      final SeatConfiguration source) {
    return (SeatConfigurationDto) conversionService.convert(source, TypeDescriptor.valueOf(SeatConfiguration.class), TypeDescriptor.valueOf(SeatConfigurationDto.class));
  }
}
```
由于此类的方法与 MapStruct 期望的签名相匹配，因此我们现在可以将其添加到 CarMapper 中，或者通过MapperSpringConfig 使用
```java
@Mapper(uses = ConversionServiceAdapter.class)
public interface CarMapper extends Converter<Car, CarDto> {
    @Mapping(target = "seats", source = "seatConfiguration")
    CarDto convert(Car car);
}

// 
@MapperConfig(componentModel = "spring", uses = ConversionServiceAdapter.class)
public interface MapperSpringConfig {}

@Mapper(config = MapperSpringConfig.class)
public interface CarMapper extends Converter<Car, CarDto> {
}
```




## 
* https://mapstruct.org/documentation/1.5/reference/html
* https://mapstruct.org/documentation/spring-extensions/reference/html
