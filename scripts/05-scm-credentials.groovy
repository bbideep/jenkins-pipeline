import com.cloudbees.plugins.credentials.impl.*;
import com.cloudbees.plugins.credentials.*;
import com.cloudbees.plugins.credentials.domains.*;

def username = new File("/run/secrets/scm-repo-user").text.trim()
def password = new File("/run/secrets/scm-repo-pwd").text.trim()

Credentials c = (Credentials) new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL,java.util.UUID.randomUUID().toString(), "scm-credentials", username, password)

SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), c)