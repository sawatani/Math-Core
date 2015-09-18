#!/bin/sh
set -eu

. $(dirname $0)/setup

pomExtra="
  <url>${MAVEN_CENTRAL_PROJECT_URL}</url>
  <licenses>
    <license>
      <name>${MAVEN_CENTRAL_LICENSE_NAME}</name>
      <url>${MAVEN_CENTRAL_LICENSE_URL}</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>${MAVEN_CENTRAL_SCM_URL}</url>
    <connection>${MAVEN_CENTRAL_SCM_CONNECTION}</connection>
  </scm>
  <developers>
    <developer>
      <id>${MAVEN_CENTRAL_DEVELOPER_ID}</id>
      <name>${MAVEN_CENTRAL_DEVELOPER_NAME}</name>
      <url>${MAVEN_CENTRAL_DEVELOPER_URL}</url>
    </developer>
  </developers>"

echo "POM Extra: $pomExtra"

mkdir -vp project
cat <<EOF >> project/plugins.sbt
 
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")
EOF

cat <<EOF > circleci-publish.sbt
////////////////
// PGP Settings for sbt-pgp plugin
 
pgpSecretRing := file("secring.gpg")
 
////////////////
// Repository Settings
 
publishMavenStyle := true
 
publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}
 
publishArtifact in Test := false
 
pomIncludeRepository := { _ => false }
 
pomExtra := (${pomExtra})
 
credentials += Credentials("Sonatype Nexus Repository Manager", 
                           "oss.sonatype.org", 
                           "${MAVEN_CENTRAL_REPOSITORY_USERNAME}",
                           "${MAVEN_CENTRAL_REPOSITORY_PASSWORD}")
EOF

echo "$MAVEN_CENTRAL_PGP_SECRING" | base64 -d > secring.gpg

echo "$MAVEN_CENTRAL_PGP_PASSPHRASE" | sbt publish-signed
