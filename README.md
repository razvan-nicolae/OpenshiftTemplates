# Overview

A template describes a set of objects that can be parameterized and processed to produce a list of objects for creation by OpenShift.

A template can be processed to create anything you have permission to create within a project, for example services, build configurations, and deployment configurations.

A template may also define a set of labels to apply to every object defined in the template.

# Uploading a Template

If you have a JSON or YAML file that defines a template you can upload the template to projects using the CLI.

This saves the template to the project for repeated use by any user with appropriate access to that project.

```
$ oc create -f <filename>
```

You can upload a template to a different project using the -n option with the name of the project:

```
$ oc create -f <filename> -n <project>
```

#Creating from Templates

## Labels

Labels are used to manage and organize generated objects, such as pods.

The labels specified in the template are applied to every object that is generated from the template.

```
$ oc process -f <filename> -l name=otherLabel
```

## Parameters

The list of parameters that you can override are listed in the parameters section of the template.

You can list them with the CLI by using the following command and specifying the file to be used:

```
$ oc process --parameters -f <filename>
```

Alternatively, if the template is already uploaded:

```
$ oc process --parameters -n openshift workshop-example
NAME                    DESCRIPTION                                                         GENERATOR           VALUE
APPLICATION_NAME        The name assigned to all of the objects defined in this template.                       workshop-example
SOURCE_REPOSITORY_URL   The URL of the repository with your application source code.                            https://github.com/orangekmt/workshop-example.git
WEBHOOK_SECRET          A secret string used to configure the Git webhook.                  expression          [a-zA-Z0-9]{40}
```

##Generating a List of Objects

Using the CLI, you can process a file defining a template to return the list of objects to standard output:

```
$ oc process -f <filename>
```

Alternatively, if the template has already been uploaded to the current project:

```
$ oc process <template_name>
```

You can create objects from a template by processing the template and piping the output to oc create:

```
$ oc process -f <filename> | oc create -f -
```

Alternatively, if the template has already been uploaded to the current project:

```
$ oc process <template> | oc create -f -
```

# Modifying an Uploaded Template

You can edit a template that has already been uploaded to your project by using the following command:

```
$ oc edit template <template>
```

#Using the Instant App and Quickstart Templates

You can list the available default Instant App and Quickstart templates with:

```
$ oc get templates -n openshift
```

By default, the templates build using a public source repository on GitHub that contains the necessary application code. In order to be able to modify the source and build your own version of the application, you must use your own repo or fork the official one.

# Writing Templates

You can define new templates to make it easy to recreate all the objects of your application. The template will define the objects it creates along with some metadata to guide the creation of those objects.

## Description

The template description covers information that informs users what your template does and helps them find it when searching in the web console.

```
kind: "Template"
apiVersion: "v1"
metadata:
  name: "workshop-example" 
  annotations:
    description: "An example To Do List Application" 
    tags: "instant-app,jboss,workshop,kermit" 
    iconClass: "icon-jboss"
```

##Labels

Templates can include a set of labels. These labels will be added to each object created when the template is instantiated.

```
kind: "Template"
apiVersion: "v1"
...
labels:
  template: "workshop-example" 
...
```

## Parameters

Parameters allow a value to be supplied by the user or generated when the template is instantiated.

```
kind: "Template"
apiVersion: "v1"
...
parameters:
  - name: APPLICATION_NAME
    displayName: Application Name
    description: The name assigned to all of the objects defined in this template 
    value: workshop-example 
    required: true 
  - name: SOURCE_REPOSITORY_URL
    displayName: Git Repository URL 
    description: The URL of the repository with your application source code 
    value: https://github.com/orangekmt/workshop-example.git 
    required: true 
  - name: WEBHOOK_SECRET
    displayName: Git Webhook Secret
    description: A secret string used to configure the GitHub webhook
    generate: expression 
    from: "[a-zA-Z0-9]{40}" 
```

## Object List

The main portion of the template is the list of objects which will be created when the template is instantiated.

This can be any valid API object, such as a BuildConfig, DeploymentConfig, Service, etc.

The object will be created exactly as defined here, with any parameter values substituted in prior to creation.

Example of a Service created by the template:

```
kind: "Template"
apiVersion: "v1"
objects:
  - kind: "Service" 
    apiVersion: "v1"
    metadata:
      name: "${APPLICATION_NAME}"
      annotations:
        description: "Exposes and load balances the application pods"
    spec:
      ports:
        - name: "web-tcp-8080"
          port: 8080
          targetPort: 8080
        - name: "web-tcp-8443"
          port: 8443
          targetPort: 8433
      selector: 
        name: ${APPLICATION_NAME}
...
```

## Creating a Template from Existing Objects

Rather than writing an entire template from scratch, you can also export existing objects from your project in template form, and then modify the template from there by adding parameters and other customizations.

```
$ oc export all --as-template=<template_name>
``` 

For the original article, please read the official documentation.
