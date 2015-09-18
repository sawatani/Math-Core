export MAVEN_CENTRAL_DEVELOPER_ID="$CIRCLE_PROJECT_USERNAME"

export GIT_DOMAIN="github.com"

export MAVEN_CENTRAL_DEVELOPER_URL="https://$GIT_DOMAIN/$MAVEN_CENTRAL_DEVELOPER_ID"
export MAVEN_CENTRAL_DEVELOPER_NAME="$(curl $MAVEN_CENTRAL_DEVELOPER_URL 2>/dev/null | grep '<title>' | sed 's/.*(\(.*\)).*/\1/')"

export MAVEN_CENTRAL_PROJECT_URL="$MAVEN_CENTRAL_DEVELOPER_URL/$CIRCLE_PROJECT_REPONAME"
export MAVEN_CENTRAL_SCM_URL="$MAVEN_CENTRAL_PROJECT_URL.git"
export MAVEN_CENTRAL_SCM_CONNECTION="scm:git:$MAVEN_CENTRAL_SCM_URL"

LICENSE="LICENSE"
if [ -f "$LICENSE" ]
then
	line1=$(cat $LICENSE | head -n1)
	line2=$(cat $LICENSE | head -n2 | tail -n1)

	if [ "$(echo "$line1" | grep 'GNU')x" == "x" ]
	then
		if [ "$(echo "$line1" | grep 'Apache')x" == "x" ]
		then
			if [ "$(echo "$line1" | grep 'MIT')x" == "x" ]
			then
				warn "No match license"
			else
				NAME="MIT License"
				URL="http://www.opensource.org/licenses/mit-license.php"
			fi
		else
			if [ "$(echo "$line2" | grep 'Version 2.0')x" == "x" ]
			then
				warn "No match license"
			else
				NAME="Apache License, Version 2.0"
				URL="http://www.apache.org/licenses/LICENSE-2.0.txt"
			fi
		fi
	else
		if [ "$(echo "$line1" | grep 'LESSER')x" == "x" ]
		then
			if [ "$(echo "$line2" | grep 'Version 3,')x" == "x" ]
			then
				if [ "$(echo "$line2" | grep 'Version 2,')x" == "x" ]
				then
					warn "No match license"
				else
					NAME="GNU General Public License Version 2 (GPL 2)"
					URL="http://www.gnu.org/licenses/gpl-2.0.txt"
				fi
			else
				NAME="GNU General Public License (GPL)"
				URL="http://www.gnu.org/licenses/gpl.txt"
			fi
		else
			NAME="GNU Lesser General Public License"
			URL="http://www.gnu.org/licenses/lgpl.txt"
		fi
	fi

	export MAVEN_CENTRAL_LICENSE_NAME="$NAME"
	export MAVEN_CENTRAL_LICENSE_URL="$URL"
fi
