document.addEventListener('DOMContentLoaded',()=>{
  const btn=document.getElementById('themeToggle');
  btn?.addEventListener('click',()=>{
    const body=document.body;
    const next = body.getAttribute('data-theme')==='dark'?'light':'dark';
    body.setAttribute('data-theme',next);
  });
  // Smooth anchor scrolling
  document.querySelectorAll('a[href^="#"]').forEach(a=>{
    a.addEventListener('click',e=>{
      const href=a.getAttribute('href');
      if(href && href.startsWith('#')){
        e.preventDefault();
        const el=document.querySelector(href);
        if(el) el.scrollIntoView({behavior:'smooth',block:'start'});
      }
    })
  })
});
