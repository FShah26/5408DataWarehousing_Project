# 5408DataWarehousing_Project
This repository is created for version controlling of the codebase for the project presented for the course 5408 - Data Management, Warehouse and Analytics. 

In this project, we've tried to create a console-based multi-user multi-location distributed Database management system.
This Database management system allows the user to perform basic SQL operations such as Create, insert, update, delete and select. Also, we've provided the features such as SQLDump extraction and ERD generation.

All the operations are concurrent and are performed at the machine where the actual table resides.
We've used Google Cloud Storage bucket as a Global Data Dictionary (GDD) and the choices of the data structure were:
* LinkedHashMap to perform operations on the data until the user commits
* HashMap was used to store the metadata of the tables for faster syntax and semantics check of the query.

Basic logging of the user and system actions is also implemented for better debugging.

Refer documentation for more details:
1. [Project Feasibility Report](https://github.com/FShah26/5408DataWarehousing_Project/blob/master/5408_Feasibility_Report%20(3).pdf)
2. [Project Final Report](https://github.com/FShah26/5408DataWarehousing_Project/blob/master/5408_Project_report.pdf)
