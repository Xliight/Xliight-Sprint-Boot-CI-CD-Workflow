mvn clean verify sonar:sonar \
  -Dsonar.login=amostakir \
  -Dsonar.password=Pcgame@06 \
  -Dsonar.projectKey=OuiTrips_CoREST \
  -Dsonar.projectName='OuiTrips_CoREST' \
  -Dsonar.host.url=https://sonar.e-ambition.app \
  -Dsonar.token=sqp_d808199ca4fd7e363a0c795c1847c0b0749671f2 \
  -Dsonar.exclusions="**/com/ouitrips/app/googlemapsservice/**","**/com/ouitrips/app/osrmservice/**"

read -p "Press any key"