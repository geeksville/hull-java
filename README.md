# Hull Java client

## Installation

Use the following dependency in your project's pom.xml

    <dependency>
        <groupId>io.hull</groupId>
        <artifactId>hull-client</artifactId>
        <version>0.1</version>
    </dependency>

If you want to compile it locally

    $ git clone git@github.com:
    $ cd hull-java
    $ mvn install

## Usage

### Configuration

If you're using Spring, you can create a bean to store your Hull configuration:

    <bean id="hullConfig" class="io.hull.HullConfiguration">
        <constructor-arg type="String"><value>YOUR_APP_ID</value></constructor-arg>
        <constructor-arg type="String"><value>YOUR_APP_SECRET</value></constructor-arg>
        <constructor-arg type="String"><value>YOUR_ORG_URL</value></constructor-arg>
    </bean>

And instantiate the related Hull helper classes in your Spring servlet.xml:

    <bean id="hullClient" class="io.hull.HullClient">
        <constructor-arg><ref bean="hullConfig"/></constructor-arg>
    </bean>

    <bean id="hullFilter" class="io.hull.filter.HullFilter">
        <constructor-arg><ref bean="hullConfig"/></constructor-arg>
    </bean>

### HullClient: Making API Calls

`get`, `put`, `post` and `delete` methods are directly available on HullClient.

You can call each by providing an endpoint path:

    hullClient.get('app');

To include parameters, include a Map<String,Object> as an argument:

    // Will convert to request params for GET/DELETEs
    Map<String, Object> params = new HashMap <String, Object> ();
    params.put("limit", 10);
    params.put("page", 2);
    hullClient.get('app/comments', params);

    // Will convert to body params for POST/PUTs
    Map<String, Object> params = new HashMap <String, Object> ();
    params.put("name", "My new name");
    hullClient.put('app', params);


To use Hull entities :

    Map<String, Object> params = new HashMap <String, Object> ();
    params.put("uid", "http://example.com");

    // Retrieve entity
    hullClient.get("entity", params);

    // Delete entity
    hullClient.delete("entity", { uid: 'http://example.com' })

    // Update entity
    params.put("name", "My new name");
    hullClient.put("entity", params);

### HullFilter: Identity the current logged in Hull user
If your app is an HTTP Servlet, you can register the HullFilter so that it's included in the filter chain for your requests.

If you're using Spring, include the filter in your web.xml by using the DelegatingFilterProxy:

    <filter>
        <filter-name>hullFilter</filter-name>
        <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
    </filter>

    <filter-mapping>
        <filter-name>hullFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

(The `filter-name` should match the id of the bean you created earlier.)

Once this is added, the request attribute "Hull-User-Id" will be available, populated with an ID if a user is logged in.

    // use the String
    String userId = request.getAttribute("Hull-User-Id");
    // or access it via the static variable
    String userId = request.getAttribute(HullFilter.HULL_USER_ID_KEY);


If you're using another framework or want to create a custom filter, you can use the methods available in HullUtils to authenticate a user.

    // Grab cookie in whatever format your framework uses
    String cookieVal = getCookieVal("hull_51acbcd12345667"); // make sure you're using your appId for the cookie name

    // Make sure the value is URL decoded
    cookieVal = urlDecode(cookieVal);

    // Authenticate the user with the given cookieVal and your appSecret
    HullUtils.authenticateUser(cookieVal, config.getAppSecret());


    
### Bring your own users

In addition to providing multiple social login options, Hull allows you to create and authenticate users that are registered within your own app.

To use this feature, you just have to add a `userHash` key at the initialization of hull.js.
You can generate this by calling HullUtils.generateUserHash(Map<String,Object> userInfo, String secret)  and including this value in your template.

You should include the basic identifiers for your user (id and email) in the userInfo Map, but you can add additional fields if necessary as well.

    Map<String, Object> params = new HashMap <String, Object> ();
    params.put("id", myUser.getId());
    params.put("email", myUser.getEmail());
    String userHash = HullUtils.generateUserHash(params, hullConfig.getAppSecret());

Read more info about the "Bring your own users" feature:
http://blog.hull.io/post/53441940108/2-ways-to-log-users-in-with-hull-and-your-own-login


## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Added some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request
