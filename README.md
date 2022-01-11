# СЕП 2021/22

Чланови тима:
 - Драгана Грбић R2 8/2021
 - Петар Николић R2 5/2021

Команде за инсталацију сертификата унутар Јаве:
1. keytool -exportcert -keystore keystore.jks -alias webshop -file keystore.crt (покренути унутар било ког пројекта који унутар свог resources фолдера садржи keystore.jks фајл) - када се појави prompt за унос лозинке, унети "password"
2. keytool -importcert -file keystore.crt -alias webshop -keystore "C:\Program Files\Java\jdk-15.0.1\lib\security\cacerts" (покренути након покретања претходне команде, у истом директоријуму, при чему путању под наводницима променити да одговара локалној инсталацији) - када се појави prompt за унос лозинке, унети "changeit"

Разлике између bank-service-1 и bank-service-2:
1. application.properties портови
2. data.sql

Разлике између card-service и qr-service:
1. application.properties

TODO:
1. још једна болница
2. qr code да буде смислени подаци
3. на два рачунара тестирати
4. тестирати јел оно истицање времена од 5 секунди ради
5. поставити да је https за фронт инсталиран локално
6. sonar qube
7. написати шта смо од оних ствари за безбедност урадили
