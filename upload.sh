#aws s3 cp ./target/halvade_upload-1.0-SNAPSHOT-jar-with-dependencies.jar s3://gcbibucket/halvade/HalvadeUploaderWithLibs.jar
#scp ./target/halvade_upload-1.0-SNAPSHOT-jar-with-dependencies.jar gcbi:/home/gcbi/workspace/halvade_run/lib/HalvadeUploaderWithLibs.jar
scp ./target/halvade_upload-1.0-SNAPSHOT-jar-with-dependencies.jar caws:/home/gcbi/workspace/halvade_run/lib/HalvadeUploaderWithLibs.jar
