mvn package
#scp ./dependencies.jar caws:/home/gcbi/workspace/halvade_run/lib/
scp ./target/halvade_upload-1.0-SNAPSHOT.jar caws:/home/gcbi/workspace/halvade_run/lib/
#aws s3 cp ./target/halvade_upload-1.0-SNAPSHOT-jar-with-dependencies.jar s3://gcbibucket/halvade/HalvadeUploaderWithLibs.jar
