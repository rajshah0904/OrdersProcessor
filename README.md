# OrdersProcessor
Project Overview:
Spearheaded the development of an advanced Java-based application designed to manage and process extensive sales orders, with each order comprising up to 10 million items. The system efficiently parses and aggregates sales data while providing detailed financial reporting.

Key Responsibilities:

- Architected and executed the OrdersProcessor Java application, handling input from the 'itemsData.txt' file with an average of 10 million items per order, specified in 'SecDataSetFive'.
- Programmed and maintained complex data structures to store and manage voluminous order data, ensuring efficient access and retrieval.
- Strategically implemented multithreading to significantly reduce processing times, as demonstrated by performance metrics: processing 4 orders in 13.803 seconds with multi-threading compared to 31.75 seconds with a single thread.
- Rigorously tested the system with an increasing number of orders (up to 6), confirming the consistent efficiency of multithreading, which maintained a nearly 2-3 fold speed advantage over single-threaded processing.
- Integrated file input/output operations for reading order details and writing comprehensive sales reports.
- Implemented error handling to manage exceptions during file operations and data processing.
- Utilized advanced Java features such as synchronized blocks and thread management to enable concurrent data processing, achieving scalability and robust performance.
- Developed a dynamic reporting system that formats and outputs the total sales and individual order costs, providing a clear summary and detailed breakdown.
