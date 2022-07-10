# Healthcare ResearchKit

The *Healthcare ResearchKit* is a software framework that makes it easy to create apps for clinical/medical research.

**sdk** module is Android client SDk for clinical study.

[TODO]

**samples** projects using sdk module are located in samples directory.

[TODO]

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

# build only sdk module
# this action generates aar(android archive package)
$ ./gradlew :sdk:build -x detekt

```


### Unit Test
```bash
# test all modules including samples
$ ./gradlew test

# test only sdk
$ ./gradlew :sdk:test
```

### Check coding Style
[compose-api-guideline](https://github.com/androidx/androidx/blob/androidx-main/compose/docs/compose-api-guidelines.md)

```bash
$ ./gradlew :sdk:ktlintCheck
```

### Static Analysis(detekt)
TODO


### API Documentation
```bash
./gradlew dokkaHtml
```
