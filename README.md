1. Установить IntelliJ IDEA Community Edition 2023.3.4

2. Установить Docker Desktop и запустить его

3. Открыть проект данный проект в IntelliJ IDEA Community Edition 2023.3.4

4. В терминале IntelliJ IDEA ввести в нем команду "docker-compose up --build" (без ковычек) - это нужно для подъема контейнеров

5. Открыть еще один терминал в IntelliJ IDEA и ввести в нем команду "java -jar ./artifacts/aqa-shop.jar" (без ковычек) для запуска самого веб-приложения

6. Открыть еще один терминал в IntelliJ IDEA и ввести команду "./gradlew allureserve" (без ковычек) - эта команда запустит автотесты через программу отчетности Allure


