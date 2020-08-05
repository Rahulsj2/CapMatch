######## Loging In ##############
cf login -a https://api.run.pivotal.io







###### Build Project ##############
mvnw package
mvnw package -Dmaven.test.skip=true


########### Push to cloud foundry ############
cf push capmatch-stage -p target/AshesiCapMatch-0.0.1-SNAPSHOT.jar