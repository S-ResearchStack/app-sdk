# Samsung Health Stack App SDK

The *Samsung Health Stack App SDK* is a software framework that makes it easy to create apps for clinical/medical research.

**kit** module is Android client kit for clinical study.

**samples** projects using kit module are located in samples directory.

## Getting Started

### Prerequisite
- [Android Studio](https://developer.android.com/studio?gclid=Cj0KCQiAip-PBhDVARIsAPP2xc2xl5x8xXFXSJbDTHF7MbTkjtZC8u2KaUBzRfDyFOfA0VrLhSADE1QaAsI1EALw_wcB&gclsrc=aw.ds)
- jvm 11

### Build
currently, detekt is not supported because of some issues.
```bash
# clean all modules
$ ./gradlew clean 

# build all modules including sample modules
$ ./gradlew build -x detekt

# build only kit module
# this action generates aar(android archive package)
$ ./gradlew :kit:build -x detekt

```


### Unit Test
```bash
# test all modules including samples
$ ./gradlew test

# test only kit
$ ./gradlew :kit:test
```

### Check coding Style
[compose-api-guideline](https://github.com/androidx/androidx/blob/androidx-main/compose/docs/compose-api-guidelines.md)

```bash
$ ./gradlew :kit:ktlintCheck
```

### API Documentation
```bash
./gradlew dokkaHtml
```
