# onetaiko-pdf-writer
Pulls a CSV of registration data and form fills it into a PDF registration form. This is used to pre-populate our registration forms on the first day of classes given the Google Form responses so that students don't need to spend much time filling out registration forms.


## Component Overview
This tool combines a few components together to build an integration pipeline:

### Google Pre-Registration Form
The pre-registration form is a google form which is linked on our main website at www.onetaiko.org. When students register for classes, they will fill out this webform at home with their information

### Google Sheets Responses
A google sheet exists which contains all of the responses from the google form. This is the form data which gets imported into the PDF forms. Form data can be loaded via a local CSV file or via a google sheets shareable link.

**NOTE**: Google sheet links will only work if it is a single sheet, as the CSV format does not support multiple sheets per file

### Registration PDF

The registration PDF was built and made editable using [LibreOffice](https://www.libreoffice.org/). Form fields can be added using this tool and given a unique reference name by right clicking the form field, clicking "Controls", and giving it a name.

### PDF Writer
Finally, this pdf writer project will combine the CSV responses into the editable PDF and generate one filled PDF per row. This is done by using [Apache PDFBox](https://pdfbox.apache.org/) to cross-reference CSV response names with editable PDF field names.

## Building the project
This project uses maven to build and package as an executable jar. You should be able to build it by running
```
mvn package
```

## Running the project
With java installed, you can execute the jar from your operating system file explorer or by running
```
java -jar <path/to/jar>
```

## Common Issues

### Libre Office

##### Text entered into field is too high / too low
Right click the form field and open the "Control" panel. Make sure the following are set appropriately:
- "Text type" should be "Single-line"
- "Vert. Allignment" should be set to Top/Bottom appropriately

##### Text entered into field is too large / small
Right click the form field and open the "Control" panel. From "Font", click on "..." for more options and set the font size appropriately