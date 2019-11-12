mvn assembly:assembly -Dmaven.test.skip=true  -DdescriptorId=jar-with-dependencies
-Dfile.encoding=UTF-8 -Dspring.profiles.active=qa
connect 'jdbc:derby:reg;bootPassword=mosip12345';

update reg.global_param set val='eng' where name='mosip.primary-language';