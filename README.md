[![Build Status](https://travis-ci.com/isopov/mongo-plan-checker.svg?branch=master)](https://travis-ci.com/isopov/mongo-plan-checker)
[![Github Actions Status](https://github.com/isopov/mongo-plan-checker/workflows/CI/badge.svg)](https://github.com/isopov/mongo-plan-checker/actions)

WIP
# Building
* Ensure docker is installed and available for current user
* `./mvnw clean verify -Pdev`
# Tag new release
* `./mvnw release:prepare -Pdev`
* `git clean -f`
