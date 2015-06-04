To package for production, run .\activator dist

To run production server in place, run .\activator clean stage

To run in production, unzip and run 
.\bin\<app_name>.bat "-Dplay.evolutions.db.default.autoApply=true" "-Dapplication.secret=<new_random_string>"
