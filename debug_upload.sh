#/bin/bash
./gradlew clean
./gradlew assembleDebug
cd ./app/build/outputs/apk/debug/
# edit buildUpdateDescription
curl -F 'buildUpdateDescription=store-redesign' -F 'file=@app-debug.apk' -F '_api_key=cbe193e61f18277395235e394f246067' https://www.pgyer.com/apiv2/app/upload