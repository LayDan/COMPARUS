# Test task for Java candidate

I wanted to implement the project with the possibility of expanding it in the future, so now it is not really clean and there is a bit of hardcode, there is no way without it because you need to slightly correct the yaml configuration


There are no tests here, sorry about that, I had a very busy weekend and some problems with OpenAPI, as I have not used it for a long time

Now I will give a short excursion on the structure of the project and how the databases are accessed in general.


1) [Request](service/src/main/java/org/multiple/datasource/configuration/db/request/Request.java) - the basic interface I use to make a request.
   Since I wanted to be able to implement other solutions in the future, it contains basic methods that the database connection server will use.
2) [RequestBuilder](service/src/main/java/org/multiple/datasource/configuration/db/request/RequestBuilder.java) - a simple interface for the builder that I will use to build queries, it is not finished, need to add validations

3) [MultipleDataSourceRequest](service/src/main/java/org/multiple/datasource/configuration/db/request/MultipleDataSourceRequest.java) - implementation of my interface together with internal builder
4) [DataBaseLayer](service/src/main/java/org/multiple/datasource/configuration/db/DataBaseLayer.java) - interface for defining common access to databases, entry point for all queries
5) [MultipleDataBaseLayer](service/src/main/java/org/multiple/datasource/configuration/db/MultipleDataBaseLayer.java) - implementation of entry point to databases
6) [DataSourceConfiguration](service/src/main/java/org/multiple/datasource/configuration/db/DataSourceConfiguration.java) - configuration, which is parsed into DTO using spring, it would be possible to use the standard method of parsing by reading the file, but there was little time
7) [DataBaseConfig](service/src/main/java/org/multiple/datasource/configuration/db/DataBaseConfig.java) - Java based configuration, create necessary beans
8) [DataSourceFactory](service/src/main/java/org/multiple/datasource/configuration/db/DataSourceFactory.java) - A factory that returns an anonymous class and implements it depending on the chosen strategy was originally conceived differently, but for now I have implemented such a draft version, in the future it is necessary to create a common container for all databases
9) [UserController](service/src/main/java/org/multiple/datasource/controller/UserController.java) - our controller, which ideally according to the technical specifications should implement the interface and use the model with openAPI, but unfortunately I had some strange problems with connecting this third-party module to the project, the project simply did not see it, but the generation was successful and this can be seen in the root build package
10) [UserService](service/src/main/java/org/multiple/datasource/service/UserService.java) - in this service you can see what I wanted to achieve, I wanted to create a convenient tool for queries, so as not to hardcode most things

I would also like to touch upon the module [processors](processors). Java Annotation Processing in this case allows us to create files that will help us fill in requests without using explicit reflection and so on.

All this is launched from the [service](service) server.

Adding tests using test containers is an interesting task, but I really didn't have time for it
In any case, it would only be useful to test that our project actually works with multiple databases. Again, apologies for the lack of tests.

I do not know if you expected such a project from me, I just used this task to think creatively and try to write a basic abstraction for such a case.

If you have any questions about the project, feel free to ask at any time. ♥