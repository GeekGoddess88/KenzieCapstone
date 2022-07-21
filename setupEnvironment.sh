# Step One- Fill out the UNIT_TWO_REPO_NAME and GITHUB_USERNAME

# Step Two - configure your shell to always have these variables.
# For OSX / Linux
# Copy and paste ALL of the properties below into your .bash_profile in your home directly

# For Windows
# Copy and paste ALL of the properties below into your .bashrc file in your home directory


# Fill out the following values
# The path of your repo on github.  Don't but the whole URL, just the part after github.com/
export CAPSTONE_REPO_NAME=ATA-Capstone-Project
export CAPSTONE_GROUP_NAME=tygary

# Do not modify the rest of these unless you have been instructed to do so.
export CAPSTONE_PROJECT_NAME=capstone
export CAPSTONE_PIPELINE_STACK=$CAPSTONE_PROJECT_NAME-$CAPSTONE_GROUP_NAME
export CAPSTONE_ARTIFACT_BUCKET=$CAPSTONE_PROJECT_NAME-$CAPSTONE_GROUP_NAME-artifacts
export CAPSTONE_APPLICATION_STACK=$CAPSTONE_PROJECT_NAME-$GITHUB_USERNAME-application
export CAPSTONE_SERVICE_STACK=$CAPSTONE_PROJECT_NAME-$GITHUB_USERNAME-service
export CAPSTONE_SERVICE_STACK_DEV=$CAPSTONE_PROJECT_NAME-$GITHUB_USERNAME-service-dev