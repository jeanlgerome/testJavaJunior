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
где, search/stat - тип операции, input.json - имя входного файла, output.json - имя выходного файла;  
входной и выходной файлы должны находится в папке json.  
Если входные параметры заданы неправильно, то сообщение об ошибке будет записано в файл по умолчанию - output.json
  
  
Дамп базы данных находится в testJavaJunior/backup/

