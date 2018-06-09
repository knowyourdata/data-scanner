
# KnowYourData - (Sensitive) File Scanner 

## Purpose

The sensitive file scanner allows you to scan your local file system for sensitive data.

It will scan the content of files for personal data like email-addresses.
You can extend this list on your own, it is built on regular expressions.
It is not limited to personal data.

It will output the results bascially in the format text file.
Additionally, JSON files are provided for visualizing the tables/columns matched as personal data.

## Installation Requirements
* Java JRE 1.7

## Usage
1.) Download the release (see releases)

2.) Build the file-scanner project:
mvn clean install

3.) Untar the  file-scanner-core-1.0-bin-release.tar

4.) Run the run.cmd/run.sh <the path to scan>
e.g. run.cmd c:/temp/

=> this will generate the result files.

5.) Fine-Tuning:
See the possible configuration files in the config directory.


## Results
The program will generate the following files:

* Text file
   * output.log = logfile of all matches
   * files_tree.txt = file structure as a tree
   
* JSON file with tree overview

=> Using the file index.html you can view the d3js / SVG diagram based on Json file.


## Screenshots

The following screenshots are based on the testdata directory.

This is how you can visualize the tables/columns holding sensitive data to get a big picture

![Sunburst diagram](xdocs/sample_testdata/sunburst.png)

Note that you can move the mouse over the arcs in the dynamic version!

## Feedback and Support

If you need support or have questions please contact us at office@fwd.at


## Commercial version
work in progress...

[FWD](http://www.fwd.at/) (by FWD GmbH).


## Credits
* SVG visualization is done using the d3js - https://d3js.org/
* The sunburst diagram is based on the sample diagram at https://bl.ocks.org/mbostock/4063423

## License

Copyright 2018 FWD GmbH

Licensed under the Apache License, Version 2.0; you may not use this file except in compliance with the License. You may obtain a copy of the License at apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

