const { execSync } = require('child_process');
const stagedGitFiles = require('staged-git-files');

stagedGitFiles((error, stagedFiles) => {
    if (error) {
        console.error(error);
        process.exit(1);
    }
    console.log('evaluating linting for ' + stagedFiles.length + ' staged files');
    for (const f of stagedFiles) {
        console.log('staged file:', f.status, f.filename);
    }
    
    lint('cosmowcl', stagedFiles.filter(
        (file) => file.status !== 'Deleted' &&
            file.filename.startsWith('src/app/') &&
            file.filename.endsWith('.ts') &&
            !file.filename.endsWith('.d.ts')
    ));

    lint('ngx-cosmo-table', stagedFiles.filter(
        (file) => file.status !== 'Deleted' &&
            file.filename.startsWith('projects/ngx-cosmo-table/src/lib') &&
            file.filename.endsWith('.ts') &&
            !file.filename.endsWith('.d.ts')
    ));
});

lint = (projectName, stagedFiles) => {
    if (!stagedFiles.length) {
        console.log(`[${projectName}] no staged files`);
        return;
    }
    let ngLint = 'ng lint ' + projectName + ' --fix ';
    for (const { filename } of stagedFiles) {
        ngLint = ngLint.concat(`--files ${filename} `);
        console.log(`[${projectName}] will lint staged file ${filename} ...`);
    }
    console.log(`[${projectName}] linting ${stagedFiles.length} staged files ...\n`);
    execSync(ngLint, { stdio: 'inherit' });
}