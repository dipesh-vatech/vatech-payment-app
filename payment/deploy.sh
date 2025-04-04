#!/bin/bash

# Stop any running application
sudo systemctl stop payments-app

# Copy the new build to the server
scp target/payments-app.jar /path/to/deployment/

# Start the updated application
sudo systemctl start payments-app