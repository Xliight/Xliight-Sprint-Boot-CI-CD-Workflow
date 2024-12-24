mvn clean verify sonar:sonar \
  -Dsonar.login=admin \
  -Dsonar.password=Pcgamelol@06 \
  -Dsonar.projectKey=APPs_CoREST \
  -Dsonar.projectName='APPs_CoREST' \
  -Dsonar.host.url=http://192.168.1.20:9000 \
  -Dsonar.token=squ_4dabdef5137a19e8e40a392f91e91f40739d115d \
  -Dsonar.exclusions="**/com/ouitrips/app/googlemapsservice/**","**/com/ouitrips/app/osrmservice/**"

read -p "Press any key"
