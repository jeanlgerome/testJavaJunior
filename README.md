"# testJavaJunior"

Инструкция по сборке/запуску:

jar и необходимые файлы с необходимой для запуска структурой находятся в testJavaJunior/out/artifacts/testJavaJunior_jar/
Необходимые файлы:
logging.properties - проперти логгера 
application.properties - проперти базы данных
папка json - здесь должны находиться входные json файлы, сюда же будут записываться выходные json файлы

Для запуска необходимо скопировать содержимое директории testJavaJunior/out/artifacts/testJavaJunior_jar/
заполнить application.properties, далее запустить jar файл через командную строку, пример: 
java -jar testJavaJunior.jar search input.json output.json


Дамп базы данных находится в testJavaJunior/backup/

