document.addEventListener('DOMContentLoaded',async()=>{
 let products=[];
 let selectedCategory='';
 let minimumRating=0;
 const list=document.getElementById('productList');
 const search=document.getElementById('searchInputSidebar');
 const sort=document.getElementById('sortSelect');
 const minPrice=document.getElementById('minPrice');
 const maxPrice=document.getElementById('maxPrice');

 function draw(items){
  list.innerHTML='';
  if(!items.length){list.innerHTML='<p class="text-muted">No products match these filters.</p>';return;}
  items.forEach(p=>{const col=document.createElement('div');col.className='col-md-3 mb-4';col.innerHTML=`<div class="card text-center h-100"><img src="${p.imageUrl}" class="card-img-top" style="height:150px;object-fit:contain"><div class="card-body"><h6>${p.brand} ${p.model}</h6><p>${p.category}</p><p class="text-danger">$${p.price.toFixed(2)}</p><div>${'★'.repeat(p.rating)}${'☆'.repeat(5-p.rating)}</div><button class="btn btn-dark btn-sm mt-2 add">Add to cart</button></div></div>`;col.querySelector('.card').onclick=e=>{if(!e.target.classList.contains('add'))location.href=`product.html?id=${p.id}`;};col.querySelector('.add').onclick=async()=>{if(!StoreApi.token()){location.href='log in.html';return;}try{await StoreApi.request('/api/cart',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({productId:p.id,quantity:1})});alert('Added to cart');}catch(e){alert(e.message);}};list.appendChild(col);});
 }

 function filter(){
  const query=search.value.trim().toLowerCase();
  const minimum=minPrice.value===''?0:Number(minPrice.value);
  const maximum=maxPrice.value===''?Infinity:Number(maxPrice.value);
  let out=products.filter(p=>(`${p.brand} ${p.model} ${p.category} ${p.description}`).toLowerCase().includes(query)
   &&(!selectedCategory||p.category.toLowerCase()===selectedCategory.toLowerCase())
   &&p.rating>=minimumRating&&p.price>=minimum&&p.price<=maximum);
  if(sort.value==='low-high')out.sort((a,b)=>a.price-b.price);
  if(sort.value==='high-low')out.sort((a,b)=>b.price-a.price);
  draw(out);
 }

 try{products=(await StoreApi.products('?size=200')).content;draw(products);}catch(e){list.innerHTML=`<p class="text-danger">${e.message}</p>`;}
 search.addEventListener('input',filter);
 sort.addEventListener('change',filter);
 minPrice.addEventListener('input',filter);
 maxPrice.addEventListener('input',filter);
 document.querySelectorAll('#categoryFilter [data-category]').forEach(link=>link.addEventListener('click',event=>{event.preventDefault();selectedCategory=link.dataset.category;document.querySelectorAll('#categoryFilter a').forEach(x=>{x.classList.remove('text-danger','fw-bold');x.classList.add('text-dark');});link.classList.remove('text-dark');link.classList.add('text-danger','fw-bold');filter();}));
 document.querySelectorAll('#starFilters [data-stars]').forEach(star=>star.addEventListener('click',()=>{minimumRating=Number(star.dataset.stars);document.querySelectorAll('#starFilters .star').forEach(x=>x.classList.remove('fw-bold','text-danger'));star.classList.add('fw-bold','text-danger');filter();}));
 document.getElementById('searchInput')?.addEventListener('input',event=>{search.value=event.target.value;filter();});
 document.getElementById('resetFiltersBtn').onclick=()=>{selectedCategory='';minimumRating=0;search.value='';sort.value='';minPrice.value='';maxPrice.value='';document.querySelectorAll('#categoryFilter a').forEach(x=>x.classList.add('text-dark'));document.querySelectorAll('#categoryFilter a,#starFilters .star').forEach(x=>x.classList.remove('text-danger','fw-bold'));draw(products);};
});
