//
//  ViewController.m
//  LHQSDKDemo
//
//  Created by xiayu on 16/9/14.
//  Copyright © 2016年 Shuang Qian Online Payment Company. All rights reserved.
//

#import "ViewController.h"
#import "ControllerActivity.h"
#import "DataSigner.h"
#import "DataVerifier.h"
#import "Base64Data.h"
#import "SecKeyWrapper.h"
#import "LHQSetting.h"

@interface ViewController ()
<UITableViewDelegate, UITableViewDataSource>

@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (strong, nonatomic) NSDictionary *dic;
- (IBAction)comfilmBtnClick:(id)sender;

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.dic = @{@"UserNo":@"110",                         // 用户编号
                 @"MerNo":@"1688850000",                        // 商户编号
                 @"CourtNo":@"100886",
                 @"PhoneNo":@"13761452540",                // 用户手机号
                 @"BillNo":@"20161220145013901",                  // 交易订单号
                 @"Amount":@"0.10",                        // 产生的交易金额
                 @"PayType":@"CJ01",                       // 支付方式
                 @"Remark":@"夏宇",                         // 备注
                 @"Products":@"支付奥比",                    // 产品说明
                 @"Province":@"省",
                 @"City":@"市区",
                 @"Block":@"行政区",
                 @"GbName":@"小区名称",
                 @"OrderType":@"交易类型",
                 @"NewFlag":@"新老客户01"
                 };
    
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.dic.allKeys.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cellID"];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:@"cellID"];
        cell.textLabel.text = self.dic.allKeys[indexPath.row];
        cell.detailTextLabel.text = self.dic.allValues[indexPath.row];
    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    /**
     *  用户编号
     *  商户号
     *  手机号
     *  交易订单号
     *  交易金额
     *  交付方式
     *  商户备注
     *  产品说明
     */
    
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)comfilmBtnClick:(id)sender {
    
//    [ControllerActivity enableTestServiceURL:YES];
    [ControllerActivity setSDKServiceURL:@"http://218.4.234.150:10080/creditsslpay/"];
    [ControllerActivity setParams:self.dic signInfoBlock:^NSString *(NSString *secretKey, NSString *signInfo, int isSignWithPrivateKey) {
        if (isSignWithPrivateKey == 0) {
            return [self decryWithPriviteKey:secretKey origaStr:signInfo];     // 解密
        } else if (isSignWithPrivateKey == 1){
            return [self encryWithPublicKey:secretKey origaStr:signInfo];      // 加密
        } else {
            return [self doRsaPriviteKey:secretKey origalStr:signInfo];                   // 签名
        }
    } resultBlock:^(NSDictionary *result) {
        NSLog(@"%@", result);
        if ([result.allKeys containsObject:@"OrderState"]) {
            UIAlertView *alerView = [[UIAlertView alloc] initWithTitle:[NSString stringWithFormat:@"返回码:%@", [result objectForKey:@"OrderState"]] message:[NSString stringWithFormat:@"返回信息:%@%@", [result objectForKey:@"StateExplain"], [result objectForKey:@"returnData"]] delegate:self cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
            [alerView show];
        } else if ([result.allKeys containsObject:@"resultCode"]) {
            UIAlertView *alerView = [[UIAlertView alloc] initWithTitle:[NSString stringWithFormat:@"返回码:%@", [result objectForKey:@"resultCode"]] message:[NSString stringWithFormat:@"返回信息:%@", [result objectForKey:@"resMess"]] delegate:self cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
            [alerView show];
        } else {
            UIAlertView *alerView = [[UIAlertView alloc] initWithTitle:@"返回信息" message:[NSString stringWithFormat:@"返回信息:%@", result] delegate:self cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
            [alerView show];
        }
    }];
}

// json字符串转成字典
- (id)objectWithJsonString:(NSString *)jsonString
{
    if (jsonString == nil) {
        return nil;
    }
    
    NSData *jsonData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    NSError *err;
    id object = [NSJSONSerialization JSONObjectWithData:jsonData
                                                options:NSJSONReadingMutableContainers
                                                  error:&err];
    if(err) {
        return nil;
    }
    return object;
}

- (NSString *)dictionaryToJson:(NSDictionary *)dic
{
    NSError *parseError = nil;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dic options:NSJSONWritingPrettyPrinted error:&parseError];
    return [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
}

// 私钥签名
-(NSString*)doRsaPriviteKey:(NSString *)priviteKey origalStr:(NSString*)string
{
    id<DataSigner> signer;
    signer = CreateRSADataSigner(priviteKey);
    NSString *signedString = [signer signString:string];
    return signedString;
}

// 公钥验签
- (BOOL)verifyRsaPublicKey:(NSString *)publicKey origalStr:(NSString *)string signStr:(NSString *)signString
{
    id<DataVerifier> verifySigner;
    verifySigner = CreateRSADataVerifier(publicKey);
    bool verifyRs = [verifySigner verifyString:string withSign:signString];
    return verifyRs;
}

//公钥加密数据
-(NSString *)encryWithPublicKey:(NSString *)publicKey origaStr:(NSString *)string
{
    //生成一个随机的8位字符串，作为des加密数据的key,对数据进行des加密，对加密后的数据用公钥再进行一次rsa加密
    NSString *encryptString = [RSAUtil encryptString: string publicKey:publicKey];
    return encryptString;
}

//私钥解密数据
-(NSString *)decryWithPriviteKey:(NSString *)priviteKey origaStr:(NSString *)string
{
    return [RSAUtil decryptString:string privateKey:priviteKey];
}

@end
