Value sources are a simple Java interface along with numerous implementation classes that allows dynamic data to be
included in XML without creating a programming language inside XML. There are many locations in the Project
where value sources are used: configuration variables, forms/dialogs, form fields, form conditionals, SQL statements,
and SQL bind parameters. Value sources allow common business logic and <i>business values</i> to be stored in shareable
instance and then used either in XML or Java files where necessary. Value sources can be either single or multiple
(list context) and are used anywhere dynamic data is required. The format of a value source is similar to a
URL (name:params).
