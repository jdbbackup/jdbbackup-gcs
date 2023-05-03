![Maven Central](https://img.shields.io/maven-central/v/com.fathzer/jdbbackup-gcs)
![License](https://img.shields.io/badge/license-Apache%202.0-brightgreen.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jdbbackup_jdbbackup-gcs&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=jdbbackup_jdbbackup-gcs)
[![javadoc](https://javadoc.io/badge2/com.fathzer/jdbbackup-gcs/javadoc.svg)](https://javadoc.io/doc/com.fathzer/jdbbackup-gcs)

# jdbbackup-gcs
A Google Cloud Storage JDBBackup destination manager

## Destination format
gcs://bucket/path

This manager uses the [default application credentials](https://cloud.google.com/docs/authentication/application-default-credentials) to authentication with Google Cloud Platform.
