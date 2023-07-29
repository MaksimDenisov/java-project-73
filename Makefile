.DEFAULT_GOAL := build-run

run-dev:
	./gradlew bootRun --args='--spring.profiles.active=dev'
report:
	./gradlew jacocoTestReport
generate-migrations:
	gradle diffChangeLog
checkstyle:
	./gradlew checkstyleMain checkstyleTest

