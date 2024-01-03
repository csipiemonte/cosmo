module.exports = {
  packages: {
    '@ckeditor/ckeditor5-angular': {
      ignorableDeepImportMatchers: [
        /@ckeditor\//
      ]
    },
    '@formio/angular': {
      ignorableDeepImportMatchers: [
        /formiojs\//
      ]
    },
  }
};

