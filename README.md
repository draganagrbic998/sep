# СЕП 2021/22

Чланови тима:
 - Драгана Грбић R2 8/2021
 - Петар Николић R2 5/2021

Команде за инсталацију сертификата унутар Јаве (неопходно да би https радио унутар zuul-gateway-a):
1. keytool -exportcert -keystore keystore.jks -alias webshop -file keystore.crt (покренути унутар било ког пројекта који унутар свог resources фолдера садржи keystore.jks фајл) - када се појави prompt за унос лозинке, унети "password"
2. keytool -importcert -file keystore.crt -alias webshop -keystore "C:\Program Files\Java\jdk-15.0.1\lib\security\cacerts" (покренути након покретања претходне команде, у истом директоријуму, при чему путању под наводницима променити да одговара локалној инсталацији) - када се појави prompt за унос лозинке, унети "changeit"

Упутство за тестирање PayPal-а:
1. инсталирати оба .cer сертификата и .der сертификат у JVM (налазе се у resources folder-у од paypal-service-а.
2. улоговати се у вебшоп као merchant1@gmail.com
3. направити ордер који има производе са ценом у EUR или USD (динар није подржан од стране sandbox PayPal-a).
