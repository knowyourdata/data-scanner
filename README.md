
# KnowYourData - (Sensitive) Data & File Scanner 

## Purpose

The sensitive data scanners allow you to scan databases and your local file system for sensitive data.

## Installation Requirements
* Java JRE 1.7

## Usage
Please see 
* ![data-scanner-core](data-scanner-core/README.md) for more information on the database scanner
* ![file-scanner-core](file-scanner-core/README.md) for more information on the file scanner 

## Screenshots database scanner

The following screenshots are based on the Northwind database ported to mysql (available for download here https://github.com/dalers/mywind).

This is how you can visualize the tables/columns holding sensitive data to get a big picture

![Sunburst diagram](data-scanner-core/xdocs/sample_northwind/sunburst.png)

Note that you can move the mouse over the arcs in the dynamic version!

## Screenshots file scanner
This is how matches in the file system are visualized:

### Sunburst diagram
Colors are based on the sensitivity of the matches in the folders/files.
The sensitivity categories can be fully customized.

![Sunburst diagram](file-scanner-core/xdocs/sample_testdata/sunburst_with_text.png)

### Tree diagram
A simple tree diagram is provided and a additionally a zoomable tree.
![Tree diagram](file-scanner-core/xdocs/sample_testdata/tree_simple.png)

## Feedback and Support
If you need support or have questions please contact us at office@fwd.at

## License

Copyright 2018 FWD GmbH

Licensed under the Apache License, Version 2.0; you may not use this file except in compliance with the License. You may obtain a copy of the License at apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

