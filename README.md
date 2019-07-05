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

The document must first be converted from PDF to the Native LibreOffice `.odg` format for Libre office to be able to make edits to the form fields. Once all form field controls have been added, LibreOffice can export it back as a new editable PDF.

If the PDF writer is filling the data in, but it is not alligning correctly within the text field cells, you can update the vertical allignment of the cell to allow the text to display correctly. Often times, the text may show up to low in the form field, so setting vertical allignment to "top" on the text field can help fix this rendering issue.

### PDF Writer
Finally, this pdf writer project will combine the CSV responses into the editable PDF and generate one filled PDF per row. This is done by using [Apache PDFBox](https://pdfbox.apache.org/) to cross-reference CSV response names with editable PDF field names.

## Updating form fields
All field mappings from CSV to PDF fields are in the FieldSetter.java file. If field names are changed, this is the file to update

## Testing the project
The project can be run easily by running the `Main.java` file and updating the file variables to point to a source CSV and target output folder.

## Building the project
This project uses maven to build. You should be able to generate an executable jar with the following commands:

1. Install maven dependencies with `mvn install`
2. Compile the project with `mvn compile`
3. Generate the jar from compiled code using `mvn assembly:single`

This should generate a jar at `./target/pdf-writer-0.0.1-jar-with-dependencies.jar`. You can then rename and/or copy this to google drive for sharing with other people.
