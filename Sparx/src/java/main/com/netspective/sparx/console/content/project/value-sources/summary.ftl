Value sources, which are simply Java classes that
follow a specific interface, provide dynamic access to common business logic and may be
considered a business rules (or more specifically a business <i>values</i>) library. Many of the
classes in the Sparx use value sources to provide values for captions, defaults,
comparisons, conditionals, and many other types of variables. Value sources
allow common business logic to be stored in reusable classes and then used
either in XML or Java files where necessary. Value sources can be either
single or multiple (list context) and are used in dialogs, fields, sql
statements, and many other places where dynamic data is required. The format of a value source
is similar to a URL (name:params).
