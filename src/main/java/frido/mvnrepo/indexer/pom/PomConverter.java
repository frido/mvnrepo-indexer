package frido.mvnrepo.indexer.pom;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Activation;
import org.apache.maven.model.ActivationFile;
import org.apache.maven.model.ActivationOS;
import org.apache.maven.model.ActivationProperty;
import org.apache.maven.model.Build;
import org.apache.maven.model.BuildBase;
import org.apache.maven.model.CiManagement;
import org.apache.maven.model.Contributor;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Developer;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.Extension;
import org.apache.maven.model.IssueManagement;
import org.apache.maven.model.License;
import org.apache.maven.model.MailingList;
import org.apache.maven.model.Model;
import org.apache.maven.model.Notifier;
import org.apache.maven.model.Organization;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.model.Prerequisites;
import org.apache.maven.model.Profile;
import org.apache.maven.model.Relocation;
import org.apache.maven.model.ReportPlugin;
import org.apache.maven.model.ReportSet;
import org.apache.maven.model.Reporting;
import org.apache.maven.model.Repository;
import org.apache.maven.model.RepositoryPolicy;
import org.apache.maven.model.Resource;
import org.apache.maven.model.Scm;
import org.apache.maven.model.Site;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.bson.Document;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PomConverter {

    static Logger log = LoggerFactory.getLogger(PomConverter.class);

    public Document valueOf(String content) {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = null;
        try {
            model = reader.read(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException | XmlPullParserException e) {
            log.error(e.getMessage(), e);
        }

        if(model == null) return null;
        Document d = new Document();
        d.put("ArtifactId", toJson(model.getArtifactId()));
        d.put("Build", toJson(model.getBuild()));
        d.put("CiManagement", toJson(model.getCiManagement()));
        d.put("Id", toJson(model.getId()));
        d.put("GroupId", toJson(model.getGroupId()));
        d.put("ModelEncoding", toJson(model.getModelEncoding()));
        d.put("ModelVersion", toJson(model.getModelVersion()));
        d.put("Description", toJson(model.getDescription()));
        d.put("InceptionYear", toJson(model.getInceptionYear()));
        d.put("Name", toJson(model.getName()));
        d.put("Packaging", toJson(model.getPackaging()));
        d.put("Url", toJson(model.getUrl()));
        d.put("Version", toJson(model.getVersion()));
        d.put("IssueManagement", toJson(model.getIssueManagement()));
        d.put("DependencyManagement", toJson(model.getDependencyManagement()));
        d.put("DistributionManagement", toJson(model.getDistributionManagement()));
        d.put("Contributors", toJsonCon(model.getContributors()));
        d.put("Developers", toJsonDev(model.getDevelopers()));
        d.put("Licenses", toJsonLic(model.getLicenses()));
        d.put("MailingLists", toJsonMai(model.getMailingLists()));
        d.put("Organization", toJson(model.getOrganization()));
        d.put("Parent", toJson(model.getParent()));
        //d.put("", toJson(model.getPomFile())); // NOTE: ignore file
        d.put("Prerequisites", toJson(model.getPrerequisites()));
        d.put("Profiles", toJsonPro(model.getProfiles()));
        //d.put("", toJson(model.getProjectDirectory())); // NOTE: ignore file
        d.put("Scm", toJson(model.getScm()));
        d.put("Dependencies", toJsonDep(model.getDependencies()));
        d.put("Modules", toJsonString(model.getModules()));
        d.put("PluginRepositories", toJsonRep(model.getPluginRepositories()));
        //d.put("", toJson(model.getProperties())); NOTE: ignore
        d.put("Repositories", toJsonRep(model.getRepositories()));
        return d;
    }

    private List<Document> toJsonDev(List<Developer> developers) {
        List<Document> out = new ArrayList<>();
        if(developers == null) return out;
        developers.forEach(developer -> out.add(toJson(developer)));
        return out;
    }

    private Document toJson(Developer developer) {
        if(developer == null) return null;
        Document d = new Document();
        d.put("Id", toJson(developer.getId()));
        d.put("Organization", toJson(developer.getOrganization()));
        // d.put("", toJson(developer.getProperties())); NOTE: ignore
        d.put("OrganizationUrl", toJson(developer.getOrganizationUrl()));
        d.put("Email", toJson(developer.getEmail()));
        d.put("Name", toJson(developer.getName()));
        d.put("Timezone", toJson(developer.getTimezone()));
        d.put("Url", toJson(developer.getUrl()));
        d.put("Roles", toJsonString(developer.getRoles()));
        return d;
    }


    private List<Document> toJsonLic(List<License> licenses) {
        List<Document> out = new ArrayList<>();
        if(licenses == null) return out;
        licenses.forEach(license -> out.add(toJson(license)));
        return out;
    }

    private Document toJson(License license) {
        if(license == null) return null;
        Document d = new Document();
        d.put("Distribution", toJson(license.getDistribution()));
        d.put("Comments", toJson(license.getComments()));
        d.put("Name", toJson(license.getName()));
        d.put("Url", toJson(license.getUrl()));
        return d;
    }

    private List<Document> toJsonMai(List<MailingList> mailingLists) {
        List<Document> out = new ArrayList<>();
        if(mailingLists == null) return out;
        mailingLists.forEach(mailingList -> out.add(toJson(mailingList)));
        return out;
    }

    private Document toJson(MailingList mailingList) {
        if(mailingList == null) return null;
        Document d = new Document();
        d.put("Archive", toJson(mailingList.getArchive()));
        d.put("Name", toJson(mailingList.getName()));
        d.put("Post", toJson(mailingList.getPost()));
        d.put("Subscribe", toJson(mailingList.getSubscribe()));
        d.put("Unsubscribe", toJson(mailingList.getUnsubscribe()));
        d.put("OtherArchives", toJsonString(mailingList.getOtherArchives()));
        return d;
    }

    private List<String> toJsonString(List<String> texts) {
        return texts;
    }

    private Document toJson(Prerequisites prerequisites) {
        if(prerequisites == null) return null;
        Document d = new Document();
        d.put("Maven", toJson(prerequisites.getMaven()));
        return d;
    }

    private Document toJson(Organization organization) {
        if(organization == null) return null;
        Document d = new Document();
        d.put("Name", toJson(organization.getName()));
        d.put("Url", toJson(organization.getUrl()));
        return d;
    }

    private Document toJson(Parent parent) {
        if(parent == null) return null;
        Document d = new Document();
        d.put("ArtifactId", toJson(parent.getArtifactId()));
        d.put("Id", toJson(parent.getId()));
        d.put("GroupId", toJson(parent.getGroupId()));
        d.put("RelativePath", toJson(parent.getRelativePath()));
        d.put("Version", toJson(parent.getVersion()));
        return d;
    }

    private List<Document> toJsonPro(List<Profile> profiles) {
        List<Document> out = new ArrayList<>();
        if(profiles == null) return out;
        profiles.forEach(profile -> out.add(toJson(profile)));
        return out;
    }

    private Document toJson(Profile profile) {
        if(profile == null) return null;
        Document d = new Document();
        d.put("DependencyManagement", toJson(profile.getDependencyManagement()));
        d.put("DistributionManagement", toJson(profile.getDistributionManagement()));
        d.put("Id", toJson(profile.getId()));
        // d.put("", toJson(profile.getProperties())); NOTE: ignore
        d.put("Source", toJson(profile.getSource()));
        d.put("Modules", toJsonString(profile.getModules()));
        d.put("Build", toJson(profile.getBuild()));
        d.put("Activation", toJson(profile.getActivation()));
        d.put("Dependencies", toJsonDep(profile.getDependencies()));
        d.put("PluginRepositories", toJsonPre(profile.getPluginRepositories()));
        d.put("Reporting", toJson(profile.getReporting()));
        d.put("Repositories", toJsonRep(profile.getRepositories()));
        return d;
    }

    private Document toJson(Reporting reporting) {
        if(reporting == null) return null;
        Document d = new Document();
        d.put("OutputDirectory", toJson(reporting.getOutputDirectory()));
        d.put("ExcludeDefaults", toJson(reporting.getExcludeDefaults()));
        d.put("Plugins", toJsonPlu(reporting.getPlugins()));
        // d.put("", toJson(reporting.getReportPluginsAsMap())); NOTE: ignore for now
        return d;
    }

    private List<Document> toJsonPlu(List<ReportPlugin> plugins) {
        List<Document> out = new ArrayList<>();
        if(plugins == null) return out;
        plugins.forEach(plugin -> out.add(toJson(plugin)));
        return out;
    }

    private Document toJson(ReportPlugin plugin) {
        if(plugin == null) return null;
        Document d = new Document();
        d.put("ArtifactId", toJson(plugin.getArtifactId()));
        d.put("GroupId", toJson(plugin.getGroupId()));
        d.put("Key", toJson(plugin.getKey()));
        d.put("Version", toJson(plugin.getVersion()));
        d.put("ReportSets", toJsonRes(plugin.getReportSets()));
        //d.put("", toJson(plugin.getReportSetsAsMap())); NOTE: ignore
        //d.put("", toJson(plugin.getConfiguration())); NOTE: ignore
        return d;
    }

    private List<Document> toJsonRes(List<ReportSet> reportSets) {
        List<Document> out = new ArrayList<>();
        if(reportSets == null) return out;
        reportSets.forEach(reportSet -> out.add(toJson(reportSet)));
        return out;
    }

    private Document toJson(ReportSet reportSet) {
        if(reportSet == null) return null;
        Document d = new Document();
        d.put("Id", toJson(reportSet.getId()));
        d.put("Reports", toJsonString(reportSet.getReports()));
        return d;
    }

    private List<Document> toJsonPre(List<Repository> pluginRepositories) {
        List<Document> out = new ArrayList<>();
        if(pluginRepositories == null) return out;
        pluginRepositories.forEach(pluginRepositorie -> out.add(toJsonPre(pluginRepositorie)));
        return out;
    }

    private Document toJsonPre(Repository pluginRepositorie) {
        if(pluginRepositorie == null) return null;
        Document d = new Document();
        d.put("Id", toJson(pluginRepositorie.getId()));
        d.put("Layout", toJson(pluginRepositorie.getLayout()));
        d.put("Name", toJson(pluginRepositorie.getName()));
        d.put("Url", toJson(pluginRepositorie.getUrl()));
        d.put("Releases", toJson(pluginRepositorie.getReleases()));
        d.put("Snapshots", toJson(pluginRepositorie.getSnapshots()));
        return d;
    }

    private Document toJson(RepositoryPolicy releases) {
        if(releases == null) return null;
        Document d = new Document();
        d.put("ChecksumPolicy", toJson(releases.getChecksumPolicy()));
        d.put("Enabled", toJson(releases.getEnabled()));
        d.put("UpdatePolicy", toJson(releases.getUpdatePolicy()));
        return d;
    }

    private Document toJson(Activation activation) {
        if(activation == null) return null;
        Document d = new Document();
        d.put("Jdk", toJson(activation.getJdk()));
        d.put("File", toJson(activation.getFile()));
        d.put("Os", toJson(activation.getOs()));
        d.put("Property", toJson(activation.getProperty()));
        return d;
    }

    private Document toJson(ActivationProperty property) {
        if(property == null) return null;
        Document d = new Document();
        d.put("Name", toJson(property.getName()));
        d.put("Value", toJson(property.getValue()));
        return d;
    }

    private Object toJson(ActivationOS os) {
        if(os == null) return null;
        Document d = new Document();
        d.put("Arch", toJson(os.getArch()));
        d.put("Family", toJson(os.getFamily()));
        d.put("Name", toJson(os.getName()));
        d.put("Version", toJson(os.getVersion()));
        return d;
    }

    private Document toJson(ActivationFile file) {
        if(file == null) return null;
        Document d = new Document();
        d.put("Exists", toJson(file.getExists()));
        d.put("Missing", toJson(file.getMissing()));
        return d;
    }

    private Document toJson(BuildBase build) {
        if(build == null) return null;
        Document d = new Document();
        d.put("Directory", toJson(build.getDirectory()));
        d.put("DefaultGoal", toJson(build.getDefaultGoal()));
        d.put("FinalName", toJson(build.getFinalName()));
        d.put("PluginManagement", toJson(build.getPluginManagement()));
        d.put("Filters", toJsonString(build.getFilters()));
        d.put("Resources", toJsonRsc(build.getResources()));
        d.put("TestResources", toJsonRsc(build.getTestResources()));
        d.put("Plugins", toJsonPgn(build.getPlugins()));
        return d;
    }

    private List<Document> toJsonPgn(List<Plugin> plugins) {
        List<Document> out = new ArrayList<>();
        if(plugins == null) return out;
        plugins.forEach(plugin -> out.add(toJson(plugin)));
        return out;
    }

    private Document toJson(Plugin plugin) {
        if(plugin == null) return null;
        Document d = new Document();
        d.put("ArtifactId", toJson(plugin.getArtifactId()));
        d.put("Id", toJson(plugin.getId()));
        d.put("GroupId", toJson(plugin.getGroupId()));
        d.put("Extensions", toJson(plugin.getExtensions()));
        d.put("Key", toJson(plugin.getKey()));
        d.put("Version", toJson(plugin.getVersion()));
        d.put("Inherited", toJson(plugin.getInherited()));
        d.put("Dependencies", toJsonDep(plugin.getDependencies()));
        d.put("Executions", toJsonExe(plugin.getExecutions()));
        return d;
    }

    private List<Document> toJsonExe(List<PluginExecution> executions) {
        List<Document> out = new ArrayList<>();
        if(executions == null) return out;
        executions.forEach(execution -> out.add(toJson(execution)));
        return out;
    }

    private Document toJson(PluginExecution execution) {
        if(execution == null) return null;
        Document d = new Document();
        d.put("Id", toJson(execution.getId()));
        d.put("Phase", toJson(execution.getPhase()));
        d.put("Inherited", toJson(execution.getInherited()));
        d.put("Goals", toJsonString(execution.getGoals()));
        d.put("Priority", toJson(execution.getPriority()));
        return d;
    }

    private int toJson(int number) {
        return number;
    }

    private List<Document> toJsonRsc(List<Resource> resources) {
        List<Document> out = new ArrayList<>();
        if(resources == null) return out;
        resources.forEach(resource -> out.add(toJson(resource)));
        return out;
    }

    private Document toJson(Resource resource) {
        if(resource == null) return null;
        Document d = new Document();
        d.put("MergeId", toJson(resource.getMergeId()));
        d.put("Directory", toJson(resource.getDirectory()));
        d.put("Filtering", toJson(resource.getFiltering()));
        d.put("TargetPath", toJson(resource.getTargetPath()));
        d.put("Excludes", toJsonString(resource.getExcludes()));
        d.put("Includes", toJsonString(resource.getIncludes()));
        return d;
    }

    private Object toJson(PluginManagement pluginManagement) {
        if(pluginManagement == null) return null;
        Document d = new Document();
        d.put("Plugins", toJsonPgn(pluginManagement.getPlugins()));
        return d;
    }

    private Document toJson(Scm scm) {
        if(scm == null) return null;
        Document d = new Document();
        d.put("Connection", toJson(scm.getConnection()));
        d.put("DeveloperConnection", toJson(scm.getDeveloperConnection()));
        d.put("Tag", toJson(scm.getTag()));
        d.put("Url", toJson(scm.getUrl()));
        return d;
    }

    private List<Document> toJsonDep(List<Dependency> dependencies) {
        List<Document> out = new ArrayList<>();
        if(dependencies == null) return out;
        dependencies.forEach(dependencie -> out.add(toJson(dependencie)));
        return out;
    }

    private Document toJson(Dependency dependencie) {
        if(dependencie == null) return null;
        Document d = new Document();
        d.put("ManagementKey", toJson(dependencie.getManagementKey()));
        d.put("ArtifactId", toJson(dependencie.getArtifactId()));
        d.put("Classifier", toJson(dependencie.getClassifier()));
        d.put("GroupId", toJson(dependencie.getGroupId()));
        d.put("Optional", toJson(dependencie.getOptional()));
        d.put("Scope", toJson(dependencie.getScope()));
        d.put("SystemPath", toJson(dependencie.getSystemPath()));
        d.put("Type", toJson(dependencie.getType()));
        d.put("Version", toJson(dependencie.getVersion()));
        d.put("Exclusions", toJsonPex(dependencie.getExclusions()));
        return d;
    }

    private List<Document> toJsonPex(List<Exclusion> exclusions) {
        List<Document> out = new ArrayList<>();
        if(exclusions == null) return out;
        exclusions.forEach(exclusion -> out.add(toJson(exclusion)));
        return out;
    }

    private Document toJson(Exclusion exclusion) {
        if(exclusion == null) return null;
        Document d = new Document();
        d.put("ArtifactId", toJson(exclusion.getArtifactId()));
        d.put("GroupId", toJson(exclusion.getGroupId()));
        return d;
    }

    private List<Document> toJsonRep(List<Repository> repositories) {
        List<Document> out = new ArrayList<>();
        if(repositories == null) return out;
        repositories.forEach(repositorie -> out.add(toJson(repositorie)));
        return out;
    }

    private Document toJson(Repository repositorie) {
        if(repositorie == null) return null;
        Document d = new Document();
        d.put("Snapshots", toJson(repositorie.getSnapshots()));
        d.put("Releases", toJson(repositorie.getReleases()));
        d.put("Url", toJson(repositorie.getUrl()));
        d.put("Name", toJson(repositorie.getName()));
        d.put("Layout", toJson(repositorie.getLayout()));
        d.put("Id", toJson(repositorie.getId()));
        return d;
    }

    private List<Document> toJsonCon(List<Contributor> contributors) {
        List<Document> out = new ArrayList<>();
        if(contributors == null) return out;
        contributors.forEach(contributor -> out.add(toJson(contributor)));
        return out;
    }

    private Document toJson(Contributor contributor) {
        if(contributor == null) return null;
        Document d = new Document();
        d.put("Organization", toJson(contributor.getOrganization()));
        // d.put("", toJson(contributor.getProperties())); NOTE: ignore
        d.put("OrganizationUrl", toJson(contributor.getOrganizationUrl()));
        d.put("Email", toJson(contributor.getEmail()));
        d.put("Name", toJson(contributor.getName()));
        d.put("Timezone", toJson(contributor.getTimezone()));
        d.put("Url", toJson(contributor.getUrl()));
        d.put("Roles", toJsonString(contributor.getRoles()));
        return d;
    }

    private Document toJson(DistributionManagement distributionManagement) {
        if(distributionManagement == null) return null;
        Document d = new Document();
        d.put("DownloadUrl", toJson(distributionManagement.getDownloadUrl()));
        d.put("Repository", toJson(distributionManagement.getRepository())); // as repository
        d.put("SnapshotRepository", toJson(distributionManagement.getSnapshotRepository())); // as repository
        d.put("Status", toJson(distributionManagement.getStatus()));
        d.put("Relocation", toJson(distributionManagement.getRelocation()));
        d.put("Site", toJson(distributionManagement.getSite()));
        return d;
    }

    private Object toJson(Site site) {
        if(site == null) return null;
        Document d = new Document();
        d.put("Id", toJson(site.getId()));
        d.put("Name", toJson(site.getName()));
        d.put("Url", toJson(site.getUrl()));
        return d;
    }

    private Object toJson(Relocation relocation) {
        if(relocation == null) return null;
        Document d = new Document();
        d.put("ArtifactId", toJson(relocation.getArtifactId()));
        d.put("GroupId", toJson(relocation.getGroupId()));
        d.put("Message", toJson(relocation.getMessage()));
        d.put("Version", toJson(relocation.getVersion()));
        return d;
    }

    private Document toJson(DependencyManagement dependencyManagement) {
        if(dependencyManagement == null) return null;
        Document d = new Document();
        d.put("Dependencies", toJsonDep(dependencyManagement.getDependencies()));
        return d;
    }

    private Document toJson(IssueManagement issueManagement) {
        if(issueManagement == null) return null;
        Document d = new Document();
        d.put("System", toJson(issueManagement.getSystem()));
        d.put("Url", toJson(issueManagement.getUrl()));
        return d;
    }

    private String toJson(String text) {
        return text;
    }

    private Document toJson(CiManagement ciManagement) {
        if(ciManagement == null) return null;
        Document d = new Document();
        d.put("System", toJson(ciManagement.getSystem()));
        d.put("Url", toJson(ciManagement.getUrl()));
        d.put("Notifiers", toJsonNot(ciManagement.getNotifiers()));
        return d;
    }

    private List<Document> toJsonNot(List<Notifier> notifiers) {
        List<Document> out = new ArrayList<>();
        if(notifiers == null) return out;
        notifiers.forEach(notifier -> out.add(toJson(notifier)));
        return out;
    }

    private Document toJson(Notifier notifier) {
        if(notifier == null) return null;
        Document d = new Document();
        d.put("Address", toJson(notifier.getAddress()));
        // d.put("", toJson(notifier.getConfiguration())); NOTE: ignore
        d.put("Type", toJson(notifier.getType()));
        return d;
    }

    private Document toJson(Build build) {
        if(build == null) return null;
        Document d = new Document();
        d.put("PluginManagement", toJson(build.getPluginManagement()));
        d.put("OutputDirectory", toJson(build.getOutputDirectory()));
        d.put("ScriptSourceDirectory", toJson(build.getScriptSourceDirectory()));
        d.put("SourceDirectory", toJson(build.getSourceDirectory()));
        d.put("TestOutputDirectory", toJson(build.getTestOutputDirectory()));
        d.put("TestSourceDirectory", toJson(build.getTestSourceDirectory()));
        d.put("DefaultGoal", toJson(build.getDefaultGoal()));
        d.put("Directory", toJson(build.getDirectory()));
        d.put("FinalName", toJson(build.getFinalName()));
        d.put("Extensions", toJsonExt(build.getExtensions()));
        d.put("Filters", toJsonString(build.getFilters()));
        d.put("Plugins", toJsonPgn(build.getPlugins()));
        d.put("Resources", toJsonRsc(build.getResources()));
        d.put("TestResources", toJsonRsc(build.getTestResources()));
        return d;
    }

    private List<Document> toJsonExt(List<Extension> extensions) {
        List<Document> out = new ArrayList<>();
        if(extensions == null) return out;
        extensions.forEach(extension -> out.add(toJson(extension)));
        return out;
    }

    private Document toJson(Extension extension) {
        if(extension == null) return null;
        Document d = new Document();
        d.put("ArtifactId", toJson(extension.getArtifactId()));
        d.put("GroupId", toJson(extension.getGroupId()));
        d.put("Version", toJson(extension.getVersion()));
        return d;
    }
}
